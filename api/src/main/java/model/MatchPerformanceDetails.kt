package model

import api.stat.analysis.model.HeadToHeadStat

/**
 * @author Josiah Kendall
 */

class MatchPerformanceDetails(val kda : ArrayList<HeadToHeadStat>,
                              val creeps : ArrayList<HeadToHeadStat>,
                              val damageDealt : ArrayList<HeadToHeadStat>,
                              val damageTaken: ArrayList<HeadToHeadStat>,
                              val gold : ArrayList<HeadToHeadStat>,
                              val xp : ArrayList<HeadToHeadStat>)
