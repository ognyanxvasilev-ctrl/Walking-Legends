# Walking Legends

Europe-first historical GPS exploration game. The current browser prototype starts in Ancient Greece (Athens), preserves local progress in the browser, and includes an interactive OpenStreetMap exploration view.

## Run the browser prototype

Open `index.html` in a modern browser. Real browser geolocation needs HTTPS or localhost, plus location permission. The interactive map loads Leaflet and OpenStreetMap tiles when online; the discovery cards remain usable if those map resources are unavailable.

For a local preview with browser location support, run:

```powershell
npm run preview
```

Then open `http://127.0.0.1:4173` on the same computer. Stop the terminal with `Ctrl+C` when finished.

## Browser safeguards

- The **Check / refresh location** button requests a fresh high-accuracy reading instead of reusing a cached GPS point.
- When offline, saved progress and discovery cards stay available; only map tiles need a connection.
- In **Character**, **Reset this device’s progress** clears local XP, artefacts, and customisation only after confirmation. It does not affect any server data because the prototype has no account system yet.

## Verify the GPS rules

```powershell
node --test
```

The discovery engine uses a 120-metre landmark radius and rejects readings with reported accuracy worse than 120 metres. See [ROADMAP.md](ROADMAP.md) for scope, completed work, and the Android delivery plan.

## Android app

The Kotlin and Jetpack Compose app is in [`app/`](app/). Read [ANDROID.md](ANDROID.md) for its location-permission boundary, current functionality, and build requirements.
