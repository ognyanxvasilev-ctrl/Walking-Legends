import test from "node:test";
import assert from "node:assert/strict";

import { STORAGE_KEY, loadProgress, normaliseProgress, resetProgress, saveProgress } from "../js/progress.js";

function memoryStorage(initial = {}) {
  const values = new Map(Object.entries(initial));
  return {
    getItem: (key) => values.get(key) ?? null,
    setItem: (key, value) => values.set(key, value),
  };
}

test("stored progress is normalised without losing valid player choices", () => {
  const state = normaliseProgress({
    xp: 250.9,
    found: [1, 1, 2, -1, "3"],
    character: { name: "  Ada  ", role: "Scholar", hair: "Wavy", skin: "Deep", outfit: "Royal Blue" },
  });

  assert.deepEqual(state, {
    xp: 250,
    found: [1, 2],
    character: { name: "Ada", role: "Scholar", hair: "Wavy", skin: "Deep", outfit: "Royal Blue" },
  });
});

test("broken local storage falls back to a safe new explorer", () => {
  const storage = memoryStorage({ [STORAGE_KEY]: "not json" });
  assert.deepEqual(loadProgress(storage), resetProgress());
});

test("progress can be saved and reset without retaining discoveries or XP", () => {
  const storage = memoryStorage();
  assert.equal(saveProgress(storage, { xp: 100, found: [1], character: { name: "Alex" } }), true);
  assert.equal(loadProgress(storage).xp, 100);
  assert.deepEqual(resetProgress().found, []);
  assert.equal(resetProgress().xp, 0);
});
