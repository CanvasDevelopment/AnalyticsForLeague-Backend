package model.match

/**
 * @author Josiah Kendall
 */
data class Participant(val participantId : Int,
                       val teamId : Int,
                       val championId : Int,
                       val spell1Id : Int,
                       val spell2Id : Int,
                       val masteries : ArrayList<Mastery>,
                       val runes : ArrayList<Rune>,
                       val highestAchievedSeasonTier : String,
                       val stats: Stats,
                       val timeline: Timeline)