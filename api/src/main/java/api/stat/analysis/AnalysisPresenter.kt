package api.stat.analysis

import api.stat.analysis.model.*
import service_contracts.ProcessingImpl

/**
 * @author Josiah Kendall
 */
class AnalysisPresenter(private val processingApi : ProcessingImpl,
                        private val analysisDao: AnalysisDao){


    fun sayHi() : String = "hi"


    // fetch card - and just pass forward the params. Then when we fetch

    /**
     * The fetch for getting the information for the creeps
     */
    fun creepsPerMinuteDeltasCard(summonerId : Long, games : Int, lane : String) : CreepsPerMinuteDeltasCard =// get creeps per minute early, mid and late for hero, for the enemy, for the villan
            analysisDao.fetchAvgCreepsPerMinStatCard(summonerId, games, lane)

    /**
     * The fetch for getting the information for the creeps
     */
    fun damagePerMinuteDeltasCard(summonerId : Long, games : Int, lane : String) : CreepsPerMinuteDeltasCard =// get creeps per minute early, mid and late for hero, for the enemy, for the villan
            analysisDao.fetchAvgCreepsPerMinStatCard(summonerId, games, lane)

    /**
     * The fetch for getting the information for the creeps per minute card detail page.
     *
     * @param summonerId    The hero summoner id.
     * @param games         The number of recent games to limit our filter to.
     * @param lane          The lane we want the info for.
     * @return A [CreepsPerMinuteDeltasDetail] object with the stats on it.
     */
    fun creepsPerMinuteDeltasDetail(summonerId : Long, games: Int, lane: String) : CreepsPerMinuteDeltasDetail {
        val creepsMaxHero = analysisDao.fetchMaxDeltas(summonerId, games, lane, HERO_CREEPS)
        val creepsMaxVillan = analysisDao.fetchMaxDeltas(summonerId, games, lane, VILLAN_CREEPS)
        val creepsMinHero = analysisDao.fetchMinDeltas(summonerId, games, lane, HERO_CREEPS)
        val creepsMinVillan = analysisDao.fetchMinDeltas(summonerId, games, lane, VILLAN_CREEPS)
        val creepsAvgHero = analysisDao.fetchAvgDeltas(summonerId, games, lane, HERO_CREEPS)
        val creepsAvgVillan = analysisDao.fetchAvgDeltas(summonerId, games, lane, VILLAN_CREEPS)

        return CreepsPerMinuteDeltasDetail(creepsMaxHero,
                creepsMaxVillan,
                creepsAvgHero,
                creepsAvgVillan,
                creepsMinHero,
                creepsMinVillan)
    }


    fun damagePerMinuteDeltasDetail(summonerId: Long, games: Int, lane: String): CreepsPerMinuteDeltasDetail {
        val maxHero = analysisDao.fetchMaxDeltas(summonerId, games, lane, HERO_DAMAGE)
        val maxVillan = analysisDao.fetchMaxDeltas(summonerId, games, lane, VILLAN_DAMAGE)
        val minHero = analysisDao.fetchMinDeltas(summonerId, games, lane, HERO_DAMAGE)
        val minVillan = analysisDao.fetchMinDeltas(summonerId, games, lane, VILLAN_DAMAGE)
        val avgHero = analysisDao.fetchAvgDeltas(summonerId, games, lane, HERO_DAMAGE)
        val avgVillan = analysisDao.fetchAvgDeltas(summonerId, games, lane, VILLAN_DAMAGE)

        return CreepsPerMinuteDeltasDetail(maxHero,
                maxVillan,
                avgHero,
                avgVillan,
                minHero,
                minVillan)
    }


}