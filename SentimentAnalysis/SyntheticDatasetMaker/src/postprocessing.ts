import Logger from "./logger.js";
import RNG from "./rng.js";
import { loadJSON, writeJSON } from "./utils.js";

const logger = new Logger("PostProcessing");

const nextRandomNumber = RNG(5000);

const rawData = loadJSON<{ story: string; tweet: string }[]>("./output.json");
const outputData = rawData.map((data, dataIndex) => {
  let tweet = data.tweet.trim();

  tweet = tweet.replace(/^"/, "").replace(/"$/, "").trim(); // remove initial and final quotes

  // a tweet may contain multiple ending hashtags. in fact, most of them do,
  // because the LLM has a tendency to include them when 'tweet' is specified
  // in its instructions.
  // real-world tweet data doesn't contain them as often.
  // so we strip them out, 1/7th of the time, with N tries.

  let trimmedHashtags = 0;
  // hashtag roussian roulette
  for (let i = 0; i < 18; i++) {
    if (nextRandomNumber() <= 1 / 7) {
      // strip it out
      tweet = tweet
        .trim()
        .replace(/#[\w']+$/m, "")
        .trim();
      trimmedHashtags++;
    }
  }
  logger.info(`#${dataIndex}: killed ${trimmedHashtags} hashtags`);

  // some tweeters prefer to type in lowercase. 1/5 chance of lowercasing
  if (nextRandomNumber() <= 1 / 5) {
    tweet = tweet.toLowerCase();
    logger.info(`#${dataIndex}: lowercased`);
  }

  // some tweeters hate punctuation. 1/7 chance of removing all punctuation
  if (nextRandomNumber() <= 1 / 7) {
    tweet = tweet.replace(/['!\.,]/g, "");
    logger.info(`#${dataIndex}: punctuation removed`);
  }

  return tweet;
});

writeJSON("./output_processed.json", outputData);
