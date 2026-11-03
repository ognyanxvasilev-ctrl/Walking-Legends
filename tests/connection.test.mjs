import test from "node:test";
import assert from "node:assert/strict";

import { getConnectionStatus } from "../js/connection.js";

test("offline mode tells players what remains available", () => {
  const status = getConnectionStatus(false);
  assert.equal(status.isOnline, false);
  assert.match(status.notice, /saved progress/i);
  assert.match(status.notice, /map tiles/i);
});

test("online mode does not display a warning", () => {
  assert.deepEqual(getConnectionStatus(true), { isOnline: true, notice: "" });
});
