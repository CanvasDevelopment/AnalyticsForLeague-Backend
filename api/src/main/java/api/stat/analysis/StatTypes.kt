package api.stat.analysis

import api.stat.analysis.model.AnalysisStatCardSkeleton

/**
 * @author Josiah Kendall
 */
interface StatTypes {
    fun getSupportStats(summonerId : Long, champId: Int) : ArrayList<AnalysisStatCardSkeleton>
    fun getTopStats(summonerId : Long, champId: Int) : ArrayList<AnalysisStatCardSkeleton>
    fun getMidStats(summonerId : Long, champId: Int) : ArrayList<AnalysisStatCardSkeleton>
    fun getJungleStats(summonerId : Long, champId: Int) : ArrayList<AnalysisStatCardSkeleton>
    fun getMarksmanStats(summonerId : Long, champId: Int) : ArrayList<AnalysisStatCardSkeleton>
}