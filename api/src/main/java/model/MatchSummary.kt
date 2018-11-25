package model

import api.stat.analysis.model.HeadToHeadStat

/**
 * @author Josiah Kendall
 */
class MatchSummary(val gameId : Long,
                   val champId : Int,
                   val enemyChampId : Int,
                   val earlyGame : HeadToHeadStat,
                   val midGame : HeadToHeadStat,
                   val lateGame : HeadToHeadStat,
                   val won : Boolean,
                   val detailsUrl : String)