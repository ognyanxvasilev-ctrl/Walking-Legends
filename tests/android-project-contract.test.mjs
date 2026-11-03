import test from "node:test";
import assert from "node:assert/strict";
import { readFile } from "node:fs/promises";

const read = (path) => readFile(new URL(`../${path}`, import.meta.url), "utf8");

test("Android project declares Compose, Kotlin, and a testable app module", async () => {
  const [settings, appBuild, versions] = await Promise.all([
    read("settings.gradle.kts"),
    read("app/build.gradle.kts"),
    read("gradle/libs.versions.toml"),
  ]);

  assert.match(settings, /include\(":app"\)/);
  assert.match(appBuild, /compose\.compiler/);
  assert.match(appBuild, /compileSdk = 37/);
  assert.match(appBuild, /testImplementation/);
  assert.match(versions, /play-services-location/);
});

test("Android manifest asks only for foreground location", async () => {
  const manifest = await read("app/src/main/AndroidManifest.xml");
  assert.match(manifest, /ACCESS_COARSE_LOCATION/);
  assert.match(manifest, /ACCESS_FINE_LOCATION/);
  assert.doesNotMatch(manifest, /ACCESS_BACKGROUND_LOCATION/);
});

test("Android domain model keeps the 120-metre discovery boundary", async () => {
  const rules = await read("app/src/main/java/com/walkinglegends/app/domain/DiscoveryRule.kt");
  const worlds = await read("app/src/main/java/com/walkinglegends/app/domain/EuropeWorlds.kt");
  assert.match(rules, /DISCOVERY_RADIUS_METRES = 120\.0/);
  assert.match(rules, /MAXIMUM_ACCEPTED_ACCURACY_METRES = 120\.0/);
  assert.match(worlds, /Ancient Greece/);
  assert.match(worlds, /Roman Europe/);
});
