package model.refined_stats

/**
 * @author Josiah Kendall
 */
data class FullGameStat(val kills : Int,
                        val deaths : Int,
                        val assists : Int,
                        val wardsPlaced : Int,
                        val wardsKilled : Int)