import SQLite from "bun:sqlite";
import Logger from "./logger";

const logger = new Logger("Database");

let db: SQLite | null = null;

export function init(fileName: string) {
  if (db !== null) {
    return;
  }

  logger.info(`Opening database: ${fileName}`);

  db = new SQLite("./database.sqlite");

  db.exec(`
    PRAGMA foreign_keys = ON;

    CREATE TABLE IF NOT EXISTS tweets (
      id INTEGER PRIMARY KEY AUTOINCREMENT,
      content TEXT NOT NULL,
      offensive BOOL NOT NULL,
      vector BLOB NOT NULL
    );
  `);

  logger.info("Database open");
}

export function pushTweet(content: string, isOffensive: boolean, vector: Float32Array) {
  const stmt = db!.prepare(`INSERT INTO tweets (content, offensive, vector) VALUES (?, ?, ?)`);

  stmt.run(content, isOffensive, Buffer.from(vector.buffer));
}

export function getAllTweetsWithoutVector(): {
  id: number;
  content: string;
  isOffensive: number;
}[] {
  //@ts-expect-error
  return db!.query(`SELECT id, content, offensive as isOffensive FROM tweets`).all();
}

export function getTweetVectorBlob(id: number): Buffer {
  //@ts-expect-error
  return Buffer.from(db!.prepare(`SELECT vector FROM tweets WHERE id = ? LIMIT 1`).get(id).vector);
}
