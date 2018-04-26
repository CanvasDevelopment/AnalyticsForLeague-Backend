package api.stat.analysis

import api.stat.analysis.model.*
import service_contracts.ProcessingImpl
import util.Constant.GameStage.EARLY_GAME
import util.Constant.GameStage.LATE_GAME
import util.Constant.GameStage.MID_GAME
import util.Constant.StatAccumulators.AVG
import util.Constant.StatAccumulators.MAX

/**
 * @author Josiah Kendall
 */
class AnalysisPresenter(private val processingApi : ProcessingImpl,
                        private val analysisDao: AnalysisDao){

    private val defaultStatTypes = DefaultStatTypes()

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
        // For each role, this will be a different type of select
        return defaultStatTypes.getDefaultStats(summonerId, lane)
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
        return defaultStatTypes.getDefaultStats(summonerId, champId, lane)
    }


    /**
     * Fetch stats for a card. This returns the card details for a summoner based on the parameters given.
     *
     * @param summonerId        The summoner id of the user
     * @param games             The number of recent games to limit this fetch to.
     * @param lane              The lane to filter our results to.
     * @param champId           The id of the champion that we want to filter/limit our results to. This means that we will
     *                          only be fetching games that the user played the champ specified by this id.
     * @param statName          The name of that select that we are interested in.
     * @param statAccumulatorType   The accumulation type. For instance, max, min or average.
     */
    fun fetchStatsForFullCard(summonerId: Long, games: Int, lane: String, champId: Int, statName : String, statAccumulatorType : String) : ArrayList<HeadToHeadStat> {
        return analysisDao.fetchDeltas(summonerId,games,lane,statName,statAccumulatorType,champId)

    }


    /**
     * Fetch stats for a card. This returns the card details for a summoner based on the parameters given.
     *
     * @param summonerId        The summoner id of the user
     * @param games             The number of recent games to limit this fetch to.
     * @param lane              The lane to filter our results to.
     * @param statName          The name of that select that we are interested in.
     * @param statAccumulatorType   The accumulation type. For instance, max, min or average.
     */
    fun fetchStatsForFullCard(summonerId: Long, games: Int, lane: String, statName : String, statAccumulatorType : String) : ArrayList<HeadToHeadStat> {
        return analysisDao.fetchDeltas(summonerId,games,lane,statName,statAccumulatorType)
    }

    /**
     * The fetch for getting the information for the creeps
     */
    fun creepsPerMinuteDeltasCard(summonerId : Long, games : Int, lane : String) : FullStatCard =// get creeps per minute early, mid and late for hero, for the enemy, for the villan
            analysisDao.fetchAvgCreepsPerMinStatCard(summonerId, games, lane)

    /**
     * Stat details are the details page for a select, for instance when we click on the "DETAILS" button on a select such
     * as creeps per minute.
     *
     * This page contains the history for that page as well as the max, the min and the total average for that select.
     *
     * Note that this page does not include a game stage, so it will be a total select. This is currently just for stats
     * like kills or assists, but will be expanded to include delta / game stage stats too, such as damage and creeps.
     *
     * @param summonerId    The id of our hero.
     * @param games         The number of games to fetch this data for. For example, to fetch data for the last 20, you
     *                      would use 20 as a parameter. To get the data for all games, use 10000, or something stupid.
     * @param lane          The lane that the summoner was playing.
     * @param statName      The name of the select that we want to fetch.
     */
    fun fetchStatDetails(summonerId: Long, games : Int, lane: String, statName: String) : StatDetails {
        val history = analysisDao.fetchPerformanceHistory(summonerId,games,lane, statName)
        val max = analysisDao.fetchStat(games,statName,lane,summonerId, MAX)
        val min = analysisDao.fetchStat(games,statName,lane,summonerId, MAX)
        val avg = analysisDao.fetchStat(games,statName,lane,summonerId, MAX)

        return StatDetails(history,max,min,avg)
    }

    /**
     * Stat details are the details page for a select, for instance when we click on the "DETAILS" button on a select such
     * as creeps per minute.
     *
     * This page contains the history for that page as well as the max, the min and the total average for that select.
     *
     * Note that this page does not include a game stage, so it will be a total select. This is currently just for stats
     * like kills or assists, but will be expanded to include delta / game stage stats too, such as damage and creeps.
     * @param summonerId    The id of our hero.
     * @param games         The number of games to fetch this data for. For example, to fetch data for the last 20, you
     *                      would use 20 as a parameter. To get the data for all games, use 10000, or something stupid.
     * @param lane          The lane that the summoner was playing.
     * @param statName      The name of the select that we want to fetch.
     * @param heroChampId   Filter to just results where the hero used this champion.
     */
    fun fetchStatDetails(summonerId: Long, games : Int, lane: String, statName: String, heroChampId : Int) : StatDetails {
        val history = analysisDao.fetchPerformanceHistory(summonerId,games,lane, statName,heroChampId)
        val max = analysisDao.fetchStat(games,statName,lane,summonerId, MAX,heroChampId)
        val min = analysisDao.fetchStat(games,statName,lane,summonerId, MAX, heroChampId)
        val avg = analysisDao.fetchStat(games,statName,lane,summonerId, MAX, heroChampId)

        return StatDetails(history,max,min,avg)
    }

    /**
     * Stat details are the details page for a select, for instance when we click on the "DETAILS" button on a select such
     * as creeps per minute.
     *
     * This page contains the history for that page as well as the max, the min and the total average for that select.
     *
     * Note that this page does not include a game stage, so it will be a total select. This is currently just for stats
     * like kills or assists, but will be expanded to include delta / game stage stats too, such as damage and creeps.
     * @param summonerId    The id of our hero.
     * @param games         The number of games to fetch this data for. For example, to fetch data for the last 20, you
     *                      would use 20 as a parameter. To get the data for all games, use 10000, or something stupid.
     * @param lane          The lane that the summoner was playing.
     * @param statName      The name of the select that we want to fetch.
     * @param heroChampId   Filter to just results where the hero used this champion.
     * @param gameStage     Indicate that only a certain game stage is required - either [EARLY_GAME], [MID_GAME] or [LATE_GAME]
     */
    fun fetchStatDetails(summonerId: Long, games : Int, lane: String, statName: String, heroChampId: Int, gameStage : String) : StatDetails {
        val history = analysisDao.fetchPerformanceHistory(summonerId,games,lane, statName, heroChampId, gameStage)
        val max = analysisDao.fetchStat(games,statName,lane,summonerId, gameStage, MAX,heroChampId)
        val min = analysisDao.fetchStat(games,statName,lane,summonerId, gameStage, MAX,heroChampId)
        val avg = analysisDao.fetchStat(games,statName,lane,summonerId, gameStage, MAX,heroChampId)

        return StatDetails(history,max,min,avg)
    }

    /**
     * Stat details are the details page for a select, for instance when we click on the "DETAILS" button on a select such
     * as creeps per minute.
     *
     * This page contains the history for that page as well as the max, the min and the total average for that select.
     *
     * Note that this page does not include a game stage, so it will be a total select. This is currently just for stats
     * like kills or assists, but will be expanded to include delta / game stage stats too, such as damage and creeps.
     * @param summonerId    The id of our hero.
     * @param games         The number of games to fetch this data for. For example, to fetch data for the last 20, you
     *                      would use 20 as a parameter. To get the data for all games, use 10000, or something stupid.
     * @param lane          The lane that the summoner was playing.
     * @param statName      The name of the select that we want to fetch.
     * @param gameStage     Indicate that only a certain game stage is required - either [EARLY_GAME], [MID_GAME] or [LATE_GAME]
     */
    fun fetchStatDetails(summonerId: Long, games : Int, lane: String, statName: String, gameStage : String) : StatDetails {
        val history = analysisDao.fetchPerformanceHistory(summonerId,games,lane, statName, gameStage)
        val max = analysisDao.fetchStat(games,statName,lane,summonerId, gameStage, MAX)
        val min = analysisDao.fetchStat(games,statName,lane,summonerId, gameStage, MAX)
        val avg = analysisDao.fetchStat(games,statName,lane,summonerId, gameStage, MAX)

        return StatDetails(history,max,min,avg)
    }



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

    /**
     * Return a single select for a "Half card" on the app.
     *
     * @param summonerId
     * @param numberOfGames
     * @param lane
     * @param champId
     * @param statName
     */
    fun fetchStatsForHalfCard(summonerId: Long, numberOfGames: Int, lane: String, champId: Int, statName: String): HeadToHeadStat {
        return analysisDao.fetchHeadToHeadStat(summonerId, numberOfGames,lane,AVG,statName,champId)
    }

    /**
     * Return a single select for a "Half card" on the app.
     *
     * @param summonerId
     * @param numberOfGames
     * @param lane
     * @param statName
     */
    fun fetchStatsForHalfCard(summonerId: Long, numberOfGames: Int, lane: String, statName: String): HeadToHeadStat {
        return analysisDao.fetchHeadToHeadStat(summonerId, numberOfGames,lane,AVG,statName)
    }



}