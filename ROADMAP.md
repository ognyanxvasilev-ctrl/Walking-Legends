# Walking Legends development roadmap

Walking Legends launches in Europe, beginning with playable Ancient Greece in Athens. Worlds are content packs, so the same progression, discovery, and museum systems can later support other European regions and continents.

## Completed in this stage

- Preserved the browser prototype and its local character, museum, XP, quest, achievement, and demo-discovery features.
- Moved Europe/world content and GPS rules into reusable browser modules.
- Added a conservative 120-metre GPS discovery rule: a landmark unlocks only when the reported point is within 120 metres and reported GPS accuracy is also 120 metres or better.
- Added automated Node tests for GPS eligibility and Europe-world data.
- Added Roman Europe, Viking Age, and Medieval Europe to the content catalogue as planned worlds; they contain no fabricated locations yet.

## Next stages

1. Replace the decorative field map with an accessible interactive OpenStreetMap-based map and landmark markers.
2. Add browser UI tests, offline handling, a manual location-refresh flow, and a reset-progress option.
3. Create the Android Gradle project with Kotlin, Jetpack Compose, location permissions, and a shared domain model matching `js/gps.js` and `js/worlds.js`.
4. Add Android unit and instrumented tests; verify debug APK installation on a real device or emulator.
5. Introduce data provenance, content authoring, and review for historic sites before adding Roman, Viking, or medieval locations.
6. Add secure authenticated accounts and server-side discovery verification before any competitive or cloud-synced progression.
7. Expand beyond Europe only after the content-pack pipeline and privacy model are validated.

## Verification record

Run `node --test` to validate the current 120-metre discovery rules and world-data invariants. Android compilation is not yet available in this workspace because neither Gradle nor an Android SDK is installed.

