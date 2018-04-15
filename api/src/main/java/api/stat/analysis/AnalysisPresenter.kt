package api.stat.analysis

import api.stat.analysis.model.*
import service_contracts.ProcessingImpl
import util.Constant.GameStage.EARLY_GAME
import util.Constant.GameStage.LATE_GAME
import util.Constant.GameStage.MID_GAME

/**
 * @author Josiah Kendall
 */
class AnalysisPresenter(private val processingApi : ProcessingImpl,
                        private val analysisDao: AnalysisDao){


    fun sayHi() : String = "hi"


    /**
     * Fetch an [ArrayList] of [AnalysisStatCardSkeleton]. Each one of these represents a card on the analysis/stats tab.
     *
     * The idea is that this list can be customised for each individual user, down to a specific role and / or champ.
     * Currently it is just pretty basic.
     *
     * @param summonerId    The id of the summoner for whom we are fetching the stats.
     * @param lane          The lane/role we are fetching the stats for.
     *
     * @return An ArrayList of card summaries which allow use to build cards and fetch the details for them in the app.
     */
    fun fetchStatList(summonerId: Long, lane : String) : ArrayList<AnalysisStatCardSkeleton> {

        // return the list for stats for the given role.
        // switch
        // For each role, this will be a different type of stat
        TODO()
    }

    /**
     * Fetch an [ArrayList] of [AnalysisStatCardSkeleton]. Each one of these represents a card on the analysis/stats tab.
     *
     * The idea is that this list can be customised for each individual user, down to a specific role and / or champ.
     * Currently it is just pretty basic.
     *
     * @param summonerId    The id of the summoner for whom we are fetching the stats.
     * @param lane          The lane/role we are fetching the stats for.
     * @param champId       A champion id - to filter results to only include matches with a specific champion.
     *
     * @return An ArrayList of card summaries which allow use to build cards and fetch the details for them in the app.
     */
    fun fetchStatList(summonerId: Long, lane: String, champId : Int) : ArrayList<AnalysisStatCardSkeleton> {
        TODO()
    }


    /**
     * Fetch stats for a card. This returns the card details for a summoner based on the parameters given.
     *
     * @param summonerId        The summoner id of the user
     * @param games             The number of recent games to limit this fetch to.
     * @param lane              The lane to filter our results to.
     * @param champId           The id of the champion that we want to filter/limit our results to. This means that we will
     *                          only be fetching games that the user played the champ specified by this id.
     * @param statName          The name of that stat that we are interested in.
     * @param statAccumulatorType   The accumulation type. For instance, max, min or average.
     */
    fun fetchStatsForCard(summonerId: Long, games: Int, lane: String, champId: Int, statName : String, statAccumulatorType : String) : ArrayList<HeadToHeadStat> {

        val earlyGame = analysisDao.fetchHeadToHeadStat(summonerId,games,lane,statAccumulatorType, EARLY_GAME,statName, champId)
        val midGame = analysisDao.fetchHeadToHeadStat(summonerId,games,lane,statAccumulatorType, MID_GAME,statName, champId)
        val lateGame = analysisDao.fetchHeadToHeadStat(summonerId,games,lane,statAccumulatorType, LATE_GAME,statName, champId)

        val result = ArrayList<HeadToHeadStat>()
        result.add(earlyGame)
        result.add(midGame)
        result.add(lateGame)

        return result
    }


    /**
     * Fetch stats for a card. This returns the card details for a summoner based on the parameters given.
     *
     * @param summonerId        The summoner id of the user
     * @param games             The number of recent games to limit this fetch to.
     * @param lane              The lane to filter our results to.
     * @param statName          The name of that stat that we are interested in.
     * @param statAccumulatorType   The accumulation type. For instance, max, min or average.
     */
    fun fetchStatsForCard(summonerId: Long, games: Int, lane: String, statName : String, statAccumulatorType : String) : ArrayList<HeadToHeadStat> {

        val earlyGame = analysisDao.fetchHeadToHeadStat(summonerId,games,lane,statAccumulatorType, EARLY_GAME,statName)
        val midGame = analysisDao.fetchHeadToHeadStat(summonerId,games,lane,statAccumulatorType, MID_GAME,statName)
        val lateGame = analysisDao.fetchHeadToHeadStat(summonerId,games,lane,statAccumulatorType, LATE_GAME,statName)

        val result = ArrayList<HeadToHeadStat>()
        result.add(earlyGame)
        result.add(midGame)
        result.add(lateGame)

        return result
    }


    /**
     * The fetch for getting the information for the creeps
     */
    fun creepsPerMinuteDeltasCard(summonerId : Long, games : Int, lane : String) : FullStatCard =// get creeps per minute early, mid and late for hero, for the enemy, for the villan
            analysisDao.fetchAvgCreepsPerMinStatCard(summonerId, games, lane)

    /**
     * The fetch for getting the information for the creeps
     */
    fun damagePerMinuteDeltasCard(summonerId : Long, games : Int, lane : String) : FullStatCard =// get creeps per minute early, mid and late for hero, for the enemy, for the villan
            analysisDao.fetchAvgCreepsPerMinStatCard(summonerId, games, lane)

    /**
     * The fetch for getting the information for the creeps per minute cardUrl detailUrl page.
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