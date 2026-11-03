# Android app foundation

`app/` is the native Android application for Walking Legends. It is a Kotlin + Jetpack Compose app with a foreground-only location permission flow and the same 120-metre discovery rule used by the browser prototype.

## Current functionality

- Requests foreground location only when the player taps **Check / refresh location**.
- Uses Android's fused location provider for a fresh high-accuracy reading.
- Rejects a discovery unless it is inside 120 metres and the reported accuracy is 120 metres or better.
- Supports playable Ancient Greece landmarks in Athens, XP, and artefact discovery in the running session.
- Lists later European worlds as planned with no fabricated landmarks.

## Build and test

Install Android Studio with Android SDK Platform 37 and JDK 17, then open this repository and run:

```powershell
gradle :app:assembleDebug :app:testDebugUnitTest
```

The repository's Android Actions workflow performs the same debug build and unit-test command on every push. This development workspace does not contain a JDK, Gradle, or Android SDK, so local APK compilation has not been claimed or performed here.

## Privacy boundary

The Android app deliberately requests no background-location permission. It reads location only after a player action; account sync and server-side anti-cheat verification remain future work.
