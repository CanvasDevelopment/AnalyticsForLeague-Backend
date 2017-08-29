package model.refined_stats

/**
 * @author Josiah Kendall
 */
data class GameStageStat(val earlyGame: Float,
                         val midGame: Float,
                         val lateGame: Float,
                         val gameId: Long)