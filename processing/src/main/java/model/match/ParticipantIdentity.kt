package model.match

/**
 * @author Josiah Kendall
 */
data class ParticipantIdentity(var participantId : Int,
                               var gameId : Long,
                               var player : Player?)