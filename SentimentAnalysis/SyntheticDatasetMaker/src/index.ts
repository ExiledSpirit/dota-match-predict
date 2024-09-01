import * as llama from "./llama.js";
import Logger from "./logger.js";
import RNG from "./rng.js";
import { delay, loadJSON, writeJSON } from "./utils.js";

const logger = new Logger("main");

function makeStoryPrompt(
  characterName: string,
  characterDescription: string,
  userName: string,
  newsEvent: string
) {
  const system = `Currently, your role is ${characterName}, described in detail below. As ${characterName}, continue the narrative exchange with ${userName}.
  
  <Guidelines>
  • Maintain the character persona but allow it to evolve with the story.
  • Be creative and proactive. Drive the story forward, introducing plotlines and events when relevant.
  • All types of outputs are encouraged; respond accordingly to the narrative.
  • Include dialogues, actions, and thoughts in each response.
  • Utilize all five senses to describe scenarios within ${characterName}'s dialogue.
  • Use emotional symbols such as "!" and "~" in appropriate contexts.
  • Incorporate onomatopoeia when suitable.
  • Allow time for ${userName} to respond with their own input, respecting their agency.
  • Act as secondary characters and NPCs as needed, and remove them when appropriate.
  • When prompted for an Out of Character [OOC:] reply, answer neutrally and in plaintext, not as ${characterName}.
  </Guidelines>
  
  <Forbidden>
  • Using excessive literary embellishments and purple prose unless dictated by ${characterName}'s persona.
  • Writing for, speaking, thinking, acting, or replying as ${userName} in your response.
  • Repetitive and monotonous outputs.
  • Positivity bias in your replies.
  • Being overly extreme or NSFW when the narrative context is inappropriate.
  </Forbidden>
  
  Follow the instructions in <Guidelines></Guidelines>, avoiding the items listed in <Forbidden></Forbidden>.
  
  Here is the description of the character ${characterName}:
  
  ${characterDescription}`;

  return [
    `<|start_header_id|>system<|end_header_id|>\n\n${system}<|eot_id|>`,
    `<|start_header_id|>user<|end_header_id|>\n\n${newsEvent}<|eot_id|>`,
    `<|start_header_id|>assistant<|end_header_id|>`,
  ].join("");
}

function makeTweetPrompt(
  characterName: string,
  characterDescription: string,
  story: string
) {
  const system = `Currently, your role is to convert the spoken dialogue of the character ${characterName} of a story into a short form tweet format, as if ${characterName} had posted it on a social media site.

  Here is the description of the character ${characterName}:
\`\`\`
${characterDescription}
\`\`\`
 
 
Here is the original spoken dialog, enclosed in backticks:
\`\`\`
${story}
\`\`\``;

  return [
    `<|start_header_id|>system<|end_header_id|>\n\n${system}<|eot_id|>`,
    `<|start_header_id|>assistant<|end_header_id|>\n\nHere is the converted tweet for the character Heisenberg:

    "`,
  ].join("");
}

const DataCharacterCodex = loadJSON<
  {
    media_type: string;
    genre: string;
    character_name: string;
    media_source: string;
    description: string;
    scenario: string;
  }[]
>("./input/character_codex.json");

const DataNews = loadJSON<
  {
    headline: string;
    content: string;
    category: string;
  }[]
>("./input/tldr_news.json");

const totalOps = DataCharacterCodex.length;
let currentOp = 0;

const nextRandomNumber = RNG(1000);

const outputs: { story: string; tweet: string }[] = [];

// Generate a synthetic tweet for every character
for (const { character_name, description } of DataCharacterCodex) {
  // Pick random headline
  const { headline, content } =
    DataNews[Math.floor(nextRandomNumber() * DataNews.length)];

  currentOp++;

  logger.info(`${currentOp}/${totalOps}: ${character_name}, ${headline}`);

  // create a narrative where the selected character reacts to the news headline
  const newsFormatted = [headline, "---", content].join("\n");
  const responseStory = await llama.requestCompletion({
    prompt: makeStoryPrompt(
      character_name,
      description,
      "Marcos",
      newsFormatted
    ),
    min_p: 0.02,
    n_predict: 128,
    stop: ["<|eot_id|>"],
    seed: Math.floor(nextRandomNumber() * 1000),
  });
  logger.info("Output Story:", responseStory);

  // convert that story into a tweet
  const responseTweet = await llama.requestCompletion({
    prompt: makeTweetPrompt(character_name, description, responseStory),
    min_p: 0.02,
    n_predict: 128,
    stop: ["<|eot_id|>", "\n", '"'],
    seed: Math.floor(nextRandomNumber() * 1000),
  });
  logger.info("Output Tweet:", responseTweet);

  outputs.push({ story: responseStory, tweet: responseTweet });

  // write current output to ramdisk every time
  writeJSON("/dev/shm/output.json", outputs);

  // write to real disk every 10th story only, to prevent disk wearout
  if (currentOp % 10 === 0) writeJSON("./output.json", outputs);

  await delay(100);
}
