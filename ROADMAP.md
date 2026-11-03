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
- Created the Kotlin + Jetpack Compose Android app foundation with foreground-only location permissions, a fused location gateway, playable Athens discovery, shared 120-metre discovery rules, and Android unit tests.

## Next stages

1. Run Android unit and instrumented tests in CI, then verify debug APK installation on a real device or emulator.
2. Add a native interactive map with reviewed OpenStreetMap-compatible tiles and accessible landmark markers.
3. Persist Android explorer progress locally using encrypted storage before introducing accounts or cloud sync.
4. Introduce data provenance, content authoring, and review for historic sites before adding Roman, Viking, or medieval locations.
5. Add secure authenticated accounts and server-side discovery verification before any competitive or cloud-synced progression.
6. Expand beyond Europe only after the content-pack pipeline and privacy model are validated.

## Verification record

Run `node --test` to validate browser behaviour and the static Android-project contract. The Android GitHub Actions workflow builds the debug app and runs its Kotlin unit tests. This workspace has no JDK, Gradle, or Android SDK, so no local APK build has been claimed.
