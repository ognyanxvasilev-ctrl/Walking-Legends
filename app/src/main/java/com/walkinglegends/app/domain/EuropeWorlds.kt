package com.walkinglegends.app.domain

data class Landmark(
    val id: String,
    val name: String,
    val subtitle: String,
    val artefact: String,
    val story: String,
    val point: GeoPoint,
)

data class HistoricalWorld(
    val id: String,
    val title: String,
    val region: String,
    val status: WorldStatus,
    val landmarks: List<Landmark>,
)

enum class WorldStatus { PLAYABLE, PLANNED }

/** Europe-first content; planned worlds remain intentionally empty until reviewed. */
object EuropeWorlds {
    val ancientGreece = HistoricalWorld(
        id = "ancient-greece",
        title = "Ancient Greece",
        region = "Athens, Greece",
        status = WorldStatus.PLAYABLE,
        landmarks = listOf(
            Landmark(
                id = "acropolis",
                name = "The Acropolis",
                subtitle = "Athens · Ancient citadel",
                artefact = "Owl of Athena",
                story = "The owl became a symbol of Athena, goddess of wisdom, and appeared on ancient Athenian coins.",
                point = GeoPoint(37.9715, 23.7257),
            ),
            Landmark(
                id = "olympian-zeus",
                name = "Temple of Olympian Zeus",
                subtitle = "Athens · Monument",
                artefact = "Laurel Crown",
                story = "Construction began in the 6th century BCE and continued across centuries.",
                point = GeoPoint(37.9693, 23.7331),
            ),
            Landmark(
                id = "ancient-agora",
                name = "Ancient Agora",
                subtitle = "Athens · Civic heart",
                artefact = "Agora Coin",
                story = "The Agora was a centre of public life for trade, debate, and gathering.",
                point = GeoPoint(37.9755, 23.7213),
            ),
        ),
    )

    val catalogue = listOf(
        ancientGreece,
        HistoricalWorld("roman-europe", "Roman Europe", "Content in research", WorldStatus.PLANNED, emptyList()),
        HistoricalWorld("viking-age", "Viking Age", "Content in research", WorldStatus.PLANNED, emptyList()),
        HistoricalWorld("medieval-europe", "Medieval Europe", "Content in research", WorldStatus.PLANNED, emptyList()),
    )
}
