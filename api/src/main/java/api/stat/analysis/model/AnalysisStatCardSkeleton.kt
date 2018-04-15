package api.stat.analysis.model

import util.Constant.AnalysisCardType

/**
 * @author Josiah Kendall
 *
 * A simple data object that represents a single statistic. This provides the app with enough information to build the
 * card and to fetch the data it needs, both for the card itself and the detail page.
 *
 * @param type      An integer representing the type of card it can be. See [AnalysisCardType]
 * @param cardUrl   A url that will fetch the correct information for this card.
 * @param detailUrl A url that will fetch the correct information for the 'details' page of this card.
 * @param title     The stat title. For instance, "Creeps Per Minute"
 */
class AnalysisStatCardSkeleton(val type : Int,
                               val cardUrl : String,
                               val detailUrl : String,
                               val title : String)