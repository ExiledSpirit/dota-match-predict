/**
 * Written by Lucas Bortoli on April 2024.
 * Module for talking with a llama.cpp server.
 */

import { fetchEventSource } from "@fortaine/fetch-event-source";
import { Logger } from "./logger.js";

const LLAMAFILE_SERVER = "http://127.0.0.1:5002";
const logger = new Logger("llm").setEnabled(false);

export interface CompletionOptions {
  prompt: string;
  temperature?: number;
  dynatemp_range?: number;
  dynatemp_exponent?: number;
  top_k?: number;
  top_p?: number;
  min_p?: number;
  n_predict?: number;
  n_keep?: number;
  stop?: string[];
  tfs_z?: number;
  typical_p?: number;
  repeat_penalty?: number;
  repeat_last_n?: number;
  penalize_nl?: boolean;
  presence_penalty?: number;
  frequency_penalty?: number;
  penalty_prompt?: string | number[] | null;
  mirostat?: number;
  mirostat_tau?: number;
  mirostat_eta?: number;
  grammar?: string;
  /*json_schema?: Record<string, any>;*/
  seed?: number;
  ignore_eos?: boolean;
  logit_bias?: Array<[number | string, number | boolean]>;
  n_probs?: number;
  min_keep?: number;
  image_data?: Array<{ data: string; id: number }>;
  id_slot?: number;
  system_prompt?: string;
  samplers?: string[];
  onToken?: (token: string) => void;
}

class RequestQueue {
  private queue: (() => Promise<void>)[] = [];
  private isProcessing: boolean = false;

  add(task: () => Promise<void>) {
    this.queue.push(task);
    this.processQueue();
  }

  private async processQueue() {
    if (this.isProcessing) return;
    this.isProcessing = true;

    while (this.queue.length > 0) {
      const task = this.queue.shift()!;
      try {
        await task();
      } catch (error) {
        logger.error("Error processing task:", error);
      }
    }

    this.isProcessing = false;
  }
}

const queue = new RequestQueue();

export async function requestCompletion(
  options: CompletionOptions,
  signal?: AbortSignal
): Promise<string> {
  return new Promise((resolve, reject) => {
    queue.add(async () => {
      try {
        logger.debug("Requesting completion", options);

        const tokens: string[] = [];

        await fetchEventSource(`${LLAMAFILE_SERVER}/completion`, {
          method: "POST",
          headers: {
            "Content-Type": "application/json",
          },
          body: JSON.stringify({
            stream: true,
            cache_prompt: true,
            ...options,
          }),
          signal,
          onmessage({ data }) {
            const { content } = JSON.parse(data) as { content: string };

            if (content) {
              tokens.push(content);

              if (options.onToken) {
                options.onToken(content);
              }
            }
          },
        });

        logger.info("Response:", tokens.join(""));

        resolve(tokens.join(""));
      } catch (error) {
        if ((error as Error).name === "AbortError") {
          logger.info("Request aborted");
        } else {
          reject(error);
        }
      }
    });
  });
}

/**
 * Returns the number of tokens in a query.
 * @param input - The input to be tokenized.
 * @returns - The amount of tokens in `query`.
 */
export async function countTokens(
  input: string,
  options?: {
    considerEosToken?: boolean;
  }
): Promise<number> {
  return new Promise((resolve, reject) => {
    queue.add(async () => {
      try {
        const response = await fetch(`${LLAMAFILE_SERVER}/tokenize`, {
          method: "POST",
          headers: {
            "content-type": "application/json",
          },
          body: JSON.stringify({
            content: input,
            add_special: options?.considerEosToken ?? false,
          }),
        }).then((r) => r.json());

        resolve(response.tokens.length);
      } catch (error) {
        reject(error);
      }
    });
  });
}

export async function createEmbedding(input: string) {
  return new Promise((resolve, reject) => {
    queue.add(async () => {
      const localLogger = logger.local("createEmbedding");
      try {
        const response = await fetch(`${LLAMAFILE_SERVER}/embedding`, {
          method: "POST",
          headers: {
            "content-type": "application/json",
          },
          body: JSON.stringify({ content: input }),
        }).then((r) => r.json());

        resolve(new Float32Array(response.embedding as number[]));
      } catch (exception) {
        if ((exception as Error).name === "NetworkError") {
          localLogger.error(
            "Embedding creation failed with a NetworkError:",
            exception
          );
          localLogger.error(
            "... Since it's a NetworkError, I'm trying again..."
          );
          resolve(await createEmbedding(input));
        }

        localLogger.error(
          "Embedding creation failed with an unknown error:",
          exception
        );
        reject(exception);
      }
    });
  });
}

export async function promptChain(
  generations: ((
    previousOutput: string
  ) => CompletionOptions | Promise<CompletionOptions>)[]
) {
  const chainLogger = logger.local("promptChain");
  let previousOutput = "";

  for (let i = 0; i < generations.length; i++) {
    chainLogger.info(`Chain: ${i + 1}/${generations.length}`);
    const generation = generations[i];
    previousOutput = await requestCompletion(await generation(previousOutput));
  }

  return previousOutput;
}

export async function countTokensSequential(
  inputs: string[],
  options?: {
    considerEosToken?: boolean;
  }
) {
  const counts: number[] = [];

  for (const input of inputs) {
    counts.push(await countTokens(input, options));
  }

  return counts;
}
