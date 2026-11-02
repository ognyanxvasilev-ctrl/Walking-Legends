# Walking Legends

Europe-first historical GPS exploration game. The current browser prototype starts in Ancient Greece (Athens) and preserves local progress in the browser.

## Run the browser prototype

Open `index.html` in a modern browser. Real browser geolocation needs HTTPS or localhost, plus location permission.

## Verify the GPS rules

```powershell
node --test
```

The discovery engine uses a 120-metre landmark radius and rejects readings with reported accuracy worse than 120 metres. See [ROADMAP.md](ROADMAP.md) for scope, completed work, and the Android delivery plan.

