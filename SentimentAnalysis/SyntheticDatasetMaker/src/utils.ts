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

export { delay };
