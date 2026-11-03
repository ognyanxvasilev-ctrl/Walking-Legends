# Walking Legends development roadmap

Walking Legends launches in Europe, beginning with playable Ancient Greece in Athens. Worlds are content packs, so the same progression, discovery, and museum systems can later support other European regions and continents.

## Completed in this stage

- Preserved the browser prototype and its local character, museum, XP, quest, achievement, and demo-discovery features.
- Moved Europe/world content and GPS rules into reusable browser modules.
- Added a conservative 120-metre GPS discovery rule: a landmark unlocks only when the reported point is within 120 metres and reported GPS accuracy is also 120 metres or better.
- Added automated Node tests for GPS eligibility and Europe-world data.
- Added Roman Europe, Viking Age, and Medieval Europe to the content catalogue as planned worlds; they contain no fabricated locations yet.
- Replaced the decorative field map with an interactive, keyboard-accessible OpenStreetMap view of Athens. It shows every playable landmark and its 120-metre discovery zone, with a non-map fallback if map resources cannot load.
- Added browser UI contract tests, offline guidance, a fresh manual location-refresh action, and a confirmed reset of local progress.

## Next stages

1. Create the Android Gradle project with Kotlin, Jetpack Compose, location permissions, and a shared domain model matching `js/gps.js`, `js/worlds.js`, `js/exploration-map.js`, and `js/progress.js`.
2. Add Android unit and instrumented tests; verify debug APK installation on a real device or emulator.
3. Introduce data provenance, content authoring, and review for historic sites before adding Roman, Viking, or medieval locations.
4. Add secure authenticated accounts and server-side discovery verification before any competitive or cloud-synced progression.
5. Expand beyond Europe only after the content-pack pipeline and privacy model are validated.

## Verification record

Run `node --test` to validate the current 120-metre discovery rules, world-data invariants, map model, local-progress behaviour, connection messaging, and browser UI contract. Android compilation is not yet available in this workspace because neither Gradle nor an Android SDK is installed.

