package model.match

/**
 * @author Josiah Kendall
 */
data class Match(val gameId : Long,
                 val platformId : String,
                 val gameCreation : Long,
                 val gameDuration : Long,
                 val queueId : Int,
                 val mapId : Int,
                 val seasonId : Int,
                 val gameVersion: String,
                 val gameMode : String,
                 val gameType : String,
                 val teams : ArrayList<Team>,
                 val participants : ArrayList<Participants>,
                 val participantIdentities: ParticipantIdentities)