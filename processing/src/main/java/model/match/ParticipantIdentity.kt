package model.match

/**
 * @author Josiah Kendall
 */
class ParticipantIdentity {
    var participantId: Int = 0
    var gameId: Long = 0
    var player: Player? = Player()

    constructor(participantId: Int, gameId: Long, player: Player?) {
        this.participantId = participantId
        this.gameId = gameId
        this.player = player
    }

    constructor()
}