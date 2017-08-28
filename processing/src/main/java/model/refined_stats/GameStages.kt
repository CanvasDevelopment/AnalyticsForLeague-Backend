package model.refined_stats

import model.stats.GameStageDelta

/**
 * @author Josiah Kendall
 *
 * Holds a list of stats for the early game, mid game, and late game for any particular stat. Examples are creeps per min,
 * damage per min etc
 *
 * @param gameStages The list of game stages. One item represents one row
 */
class GameStages(val gameStages: ArrayList<GameStageDelta>,
                 val tableName: String) : RefinedStatContract {
    override fun save(summonerId: Long) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}