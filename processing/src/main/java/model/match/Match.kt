package model.match

import com.google.gson.annotations.SerializedName
import java.util.*
import javax.print.attribute.standard.DateTimeAtCreation

/**
 * @author Josiah Kendall
 */
class Match {
    var gameId : Long = 0
    var platformId : String = ""
    var gameCreation : Long = 0
    var gameDuration : Long = 0
    var queueId : Int = 0
    var mapId : Int = 0
    var seasonId : Int = 0
    var gameVersion: String = ""
    var gameMode : String = ""
    var gameType : String = ""
    var teams : ArrayList<Team> = ArrayList()
    var participants : ArrayList<Participant> = ArrayList()
    var participantIdentities: ArrayList<ParticipantIdentity> = ArrayList()

    constructor()

    public constructor(gameId : Long,
                       platformId : String,
                       gameCreation: Long,
                       gameDuration : Long,
                       queueId : Int,
                       mapId : Int,
                       seasonId : Int,
                       gameVersion : String,
                       gameMode : String,
                       gameType : String,
                       teams : ArrayList<Team>,
                       participants : ArrayList<Participant>,
                       participantIdentities : ArrayList<ParticipantIdentity>) {
        this.gameId = gameId
        this.participantIdentities = participantIdentities
        this.gameType = gameType
        this.gameMode = gameMode
        this.mapId = mapId
        this.queueId = queueId
        this.gameDuration = gameDuration
        this.gameCreation = gameCreation
        this.platformId = platformId
        this.seasonId = seasonId
        this.gameVersion = gameVersion
        this.teams = teams
        this.participants = participants
        this.participantIdentities = participantIdentities
    }
}

