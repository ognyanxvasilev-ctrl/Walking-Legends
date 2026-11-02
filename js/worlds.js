/**
 * Content is organised by world rather than by screen. This keeps the Europe
 * launch content portable to the Android app and lets later continents plug in
 * without changing discovery, progression, or museum rules.
 */
export const EUROPE_WORLDS = [
  {
    id: "ancient-greece",
    continent: "Europe",
    title: "Ancient Greece",
    region: "Athens, Greece",
    status: "playable",
    summary: "Discover the civic heart of classical Athens.",
    places: [
      {
        id: 1,
        name: "The Acropolis",
        subtitle: "Athens · Ancient citadel",
        icon: "🏛️",
        artefact: "Owl of Athena",
        lat: 37.9715,
        lon: 23.7257,
        story:
          "The owl became a symbol of Athena, goddess of wisdom. Its image appeared on ancient Athenian coins carried throughout the Mediterranean.",
      },
      {
        id: 2,
        name: "Temple of Olympian Zeus",
        subtitle: "Athens · Monument",
        icon: "🏺",
        artefact: "Laurel Crown",
        lat: 37.9693,
        lon: 23.7331,
        story:
          "Construction of this temple began in the 6th century BCE and continued across centuries. It was once among the largest temples in the ancient world.",
      },
      {
        id: 3,
        name: "Ancient Agora",
        subtitle: "Athens · Civic heart",
        icon: "🪙",
        artefact: "Agora Coin",
        lat: 37.9755,
        lon: 23.7213,
        story:
          "The Agora was a centre of public life where people traded, debated, and gathered. It helped shape the civic culture of classical Athens.",
      },
    ],
  },
  {
    id: "roman-europe",
    continent: "Europe",
    title: "Roman Europe",
    region: "Content in research",
    status: "planned",
    summary: "A future world spanning roads, forums, and frontier settlements.",
    places: [],
  },
  {
    id: "viking-age",
    continent: "Europe",
    title: "Viking Age",
    region: "Content in research",
    status: "planned",
    summary: "A future world of coastal trade, voyages, and settlements.",
    places: [],
  },
  {
    id: "medieval-europe",
    continent: "Europe",
    title: "Medieval Europe",
    region: "Content in research",
    status: "planned",
    summary: "A future world of cities, crafts, and pilgrimage routes.",
    places: [],
  },
];

export const ANCIENT_GREECE = EUROPE_WORLDS.find((world) => world.id === "ancient-greece");

