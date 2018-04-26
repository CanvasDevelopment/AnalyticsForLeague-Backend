package api.stat.analysis.model

/**
 * @author Josiah Kendall
 */
data class StatDetails(val statHistory : ArrayList<HeadToHeadStat>,
           val statMax : Float,
           val statMin : Float,
           val statAvg : Float)