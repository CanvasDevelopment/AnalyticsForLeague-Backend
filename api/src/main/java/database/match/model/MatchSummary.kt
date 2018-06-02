package database.match.model

class MatchSummary {
    var id: Int = 0
    var platformId: String? = null
    var gameId: Long = 0
    var champion: Int = 0
    var queue: Int = 0
    var season: Int = 0
    var timestamp: Long = 0
    var role: String? = null
    var lane: String? = null
    var summonerId: Long = 0

    constructor() {
        // ?? why is this here?
    }

    constructor(id: Int,
                platformId: String,
                gameId: Long,
                champion: Int,
                queue: Int,
                season: Int,
                timestamp: Long,
                role: String,
                lane: String,
                summonerId: Long) {
        this.champion = champion
        this.gameId = gameId
        this.queue = queue
        this.id = id
        this.platformId = platformId
        this.season = season
        this.timestamp = timestamp
        this.role = role
        this.lane = lane
        this.summonerId = summonerId
    }

}
