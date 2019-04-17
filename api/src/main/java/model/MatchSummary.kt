package model

import api.stat.analysis.model.HeadToHeadStat

/**
 * @author Josiah Kendall
 */
class MatchSummary(val matchId : Long,
                   val champId : Int,
                   val enemyChampId : Int,
                   val earlyGame : HeadToHeadStat,
                   val midGame : HeadToHeadStat,
                   val lateGame : HeadToHeadStat,
                   val won : Boolean,
                   val detailsUrl : String,
                   val summonerId : String)