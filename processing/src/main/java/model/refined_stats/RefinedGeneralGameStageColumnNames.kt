package model.refined_stats

/**
 * @author Josiah Kendall
 *
 * A model to store column names for a game stage. Use this to set specific column names
 *  e.g:
 *  - "heroXpMidGame"
 *  - "villanCreepsEarlyGame"
 */
class RefinedGeneralGameStageColumnNames(val earlyGame : String, val midGame : String, val lateGame : String)