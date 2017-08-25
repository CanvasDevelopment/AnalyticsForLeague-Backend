package model.refined_stats

import model.stats.GameStageDelta

/**
 * @author Josiah Kendall
 */
class GameStages(val gameStages : ArrayList<GameStageDelta>,
                 val summonerId : Long) : RefinedStatContract {
    override fun save() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}