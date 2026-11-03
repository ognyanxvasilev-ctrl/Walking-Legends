import test from "node:test";
import assert from "node:assert/strict";
import { readFile } from "node:fs/promises";

const page = await readFile(new URL("../index.html", import.meta.url), "utf8");

test("browser UI exposes an accessible live connection warning", () => {
  assert.match(page, /id="connectionBanner"[^>]*role="status"[^>]*aria-live="polite"/);
});

test("browser UI offers location refresh and a confirmed local reset action", () => {
  assert.match(page, /id="locationRefreshButton"[^>]*onclick="refreshLocation\(\)"/);
  assert.match(page, /id="resetProgressButton"[^>]*onclick="resetLocalProgress\(\)"/);
  assert.match(page, /function resetLocalProgress\(\).*window\.confirm/s);
});
