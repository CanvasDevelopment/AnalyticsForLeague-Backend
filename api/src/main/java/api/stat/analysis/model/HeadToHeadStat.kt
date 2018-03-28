package api.stat.analysis.model

/**
 * @author Josiah Kendall
 *
 * A class to hold a head to head value of a stat - our heros score and their opponents score.
 */
data class HeadToHeadStat(val enemyStatValue : Float,
                          val heroStatValue : Float)