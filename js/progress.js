export const STORAGE_KEY = "wl-state";

const CHARACTER_CHOICES = {
  role: ["Scout", "Scholar", "Guardian"],
  hair: ["Short", "Wavy", "Long", "Curly"],
  skin: ["Light", "Medium", "Deep"],
  outfit: ["Forest Green", "Royal Blue", "Burgundy", "Sand"],
};

export function createDefaultProgress() {
  return {
    xp: 0,
    found: [],
    character: {
      name: "New Explorer",
      role: "Scout",
      hair: "Short",
      skin: "Medium",
      outfit: "Forest Green",
    },
  };
}

function allowedChoice(value, choices, fallback) {
  return choices.includes(value) ? value : fallback;
}

/** Normalises old or manually edited browser storage into a safe game state. */
export function normaliseProgress(candidate) {
  const defaults = createDefaultProgress();
  const source = candidate && typeof candidate === "object" ? candidate : {};
  const character = source.character && typeof source.character === "object" ? source.character : {};
  const found = Array.isArray(source.found)
    ? [...new Set(source.found.filter((id) => Number.isInteger(id) && id > 0))]
    : [];
  const name = typeof character.name === "string" ? character.name.trim().slice(0, 22) : defaults.character.name;

  return {
    xp: Number.isFinite(source.xp) && source.xp >= 0 ? Math.floor(source.xp) : 0,
    found,
    character: {
      name: name || defaults.character.name,
      role: allowedChoice(character.role, CHARACTER_CHOICES.role, defaults.character.role),
      hair: allowedChoice(character.hair, CHARACTER_CHOICES.hair, defaults.character.hair),
      skin: allowedChoice(character.skin, CHARACTER_CHOICES.skin, defaults.character.skin),
      outfit: allowedChoice(character.outfit, CHARACTER_CHOICES.outfit, defaults.character.outfit),
    },
  };
}

export function loadProgress(storage) {
  try {
    return normaliseProgress(JSON.parse(storage?.getItem(STORAGE_KEY) || "{}"));
  } catch {
    return createDefaultProgress();
  }
}

export function saveProgress(storage, progress) {
  try {
    storage?.setItem(STORAGE_KEY, JSON.stringify(normaliseProgress(progress)));
    return true;
  } catch {
    return false;
  }
}

/** Returns a new clean local progress state; the caller decides when to save it. */
export function resetProgress() {
  return createDefaultProgress();
}
