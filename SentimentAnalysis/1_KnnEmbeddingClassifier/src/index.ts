import { bufferToFloat32Array, cosineSimilarity } from "./cosineSimilarity.js";
import * as DB from "./database.js";
import Logger from "./logger.js";
import RNG, { createEmbedding, delay, loadJSON, trainTestSplit, writeJSON } from "./utils.js";

const logger = new Logger("main");

const rawOff = loadJSON<string[]>("../Dataset/OffensiveTweets.json");
const rawInoffensive = loadJSON<string[]>("../0_SyntheticDatasetMaker/output_processed.json");

const offensive = trainTestSplit(rawOff, 0.8, RNG(1000));
const inoffensive = trainTestSplit(rawInoffensive, 0.8, RNG(1000));

logger.info(`# Offensive: ${offensive.train.length} train, ${offensive.test.length} test`);
logger.info(`# Inoffensive: ${inoffensive.train.length} train, ${inoffensive.test.length} test`);

DB.init("./database.sqlite");

async function createEmbeddingDatabase() {
  for (const [index, tweet] of inoffensive.train.entries()) {
    logger.info(`Creating embedding (not offensive): ${index + 1}/${inoffensive.train.length}`);
    DB.pushTweet(tweet, false, await createEmbedding(tweet));
  }

  for (const [index, tweet] of offensive.train.entries()) {
    logger.info(`Creating embedding (offensive): ${index + 1}/${offensive.train.length}`);
    DB.pushTweet(tweet, true, await createEmbedding(tweet));
  }
}

async function compareOne(prompt: string): Promise<{
  comparedAgainst: string;
  isOffensive: boolean;
  score: number;
}> {
  const promptVector = await createEmbedding(prompt);

  const comparisonResults: {
    comparedAgainst: string;
    isOffensive: boolean;
    score: number;
  }[] = [];

  // Since each tweet object is at least 5 kB, don't read all at once
  // Read only the IDs for now... Maybe implement pagination later?
  for (const other of DB.getAllTweetsWithoutVector()) {
    const thisVector = bufferToFloat32Array(DB.getTweetVectorBlob(other.id));
    const score = cosineSimilarity(promptVector, thisVector);

    comparisonResults.push({
      comparedAgainst: other.content,
      isOffensive: other.isOffensive === 1,
      score: score,
    });
  }

  // Sort by score, descending
  comparisonResults.sort((a, b) => b.score - a.score);

  const topMatch = comparisonResults[0];

  return topMatch;
}

async function evaluateAll() {
  interface Evaluation {
    prediction: boolean;
    actual: boolean;
    score: number;
  }

  const evaluations: Evaluation[] = [];

  for (const [index, tweet] of inoffensive.test.entries()) {
    const prediction = await compareOne(tweet);

    const evaluation: Evaluation = {
      actual: false,
      prediction: prediction.isOffensive,
      score: prediction.score,
    };

    logger.info(
      `predict: ${evaluation.prediction}, actual ${evaluation.actual}, score ${evaluation.score}`
    );

    evaluations.push(evaluation);
  }

  for (const [index, tweet] of offensive.test.entries()) {
    const prediction = await compareOne(tweet);

    const evaluation: Evaluation = {
      actual: true,
      prediction: prediction.isOffensive,
      score: prediction.score,
    };

    logger.info(
      `predict: ${evaluation.prediction}, actual ${evaluation.actual}, score ${evaluation.score}`
    );

    evaluations.push(evaluation);
  }

  writeJSON("./evals.json", evaluations);
}

// await evaluateAll();
