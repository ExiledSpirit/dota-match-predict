import fs from "node:fs";
import { setTimeout as delay } from "node:timers/promises";

export function loadJSON<T extends object>(filePath: string): T {
  return JSON.parse(fs.readFileSync(filePath, "utf-8")) as T;
}

export function writeJSON<T extends object>(filePath: string, data: T) {
  return fs.writeFileSync(filePath, JSON.stringify(data, null, "  "), {
    encoding: "utf-8",
  });
}

/**
 * Creates a pseudo-random value generator. The seed must be an integer.
 *
 * Uses an optimized version of the Park-Miller PRNG.
 * http://www.firstpr.com.au/dsp/rand31/
 */
export default function RNG(seed: number) {
  let _seed = seed % 2147483647;
  if (_seed <= 0) _seed += 2147483646;

  const next = function () {
    return (_seed = (_seed * 16807) % 2147483647);
  };

  /**
   * Returns a pseudo-random floating point number in range [0, 1).
   */
  const nextFloat = function () {
    // We know that result of next() will be 1 to 2147483646 (inclusive).
    return (next() - 1) / 2147483646;
  };

  return nextFloat;
}

export function trainTestSplit<T>(
  data: T[],
  trainSliceTotalPercent: number,
  rng: () => number
): { train: T[]; test: T[] } {
  // Shuffle the data array using Fisher-Yates algorithm
  const shuffledData = [...data];
  for (let i = shuffledData.length - 1; i > 0; i--) {
    const j = Math.floor(rng() * (i + 1)); // Use provided random number generator
    [shuffledData[i], shuffledData[j]] = [shuffledData[j], shuffledData[i]];
  }

  // Determine the split index
  const splitIndex = Math.floor(trainSliceTotalPercent * shuffledData.length);

  // Split the data into train and test sets
  const train = shuffledData.slice(0, splitIndex);
  const test = shuffledData.slice(splitIndex);

  return { train, test };
}

export async function createEmbedding(input: string) {
  const response = await fetch(`http://127.0.0.1:6600/embedding`, {
    method: "POST",
    headers: {
      "content-type": "application/json",
    },
    body: JSON.stringify({ content: input }),
  }).then((r) => r.json());

  return new Float32Array(response.embedding as number[]);
}

export { delay };
