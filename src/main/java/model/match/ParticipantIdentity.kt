package model.match

/**
 * @author Josiah Kendall
 */
data class ParticipantIdentity(val participantId : Int,
                               val gameId : Long,
                               val player : Player)