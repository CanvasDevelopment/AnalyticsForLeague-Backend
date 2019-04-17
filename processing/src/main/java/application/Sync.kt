package application

import application.domain.MatchControl
import model.SyncProgress
import util.*

/**
 * @author Josiah Kendall
 */
class Sync (val matchControl: MatchControl) {


    fun refineStats(summonerId: String) : Int {
        val savedTop = matchControl.refineMatchData(summonerId, SOLO, TOP)
        val savedMid = matchControl.refineMatchData(summonerId, SOLO, MID)
        val savedJungle = matchControl.refineMatchData(summonerId, NONE, JUNGLE)
        val savedSup = matchControl.refineMatchData(summonerId, DUO_SUPPORT, BOT)
        val savedAdc = matchControl.refineMatchData(summonerId, DUO_CARRY, BOT)

        if (savedJungle &&  savedAdc && savedMid && savedSup && savedTop) {
            // Clear out the data to save space and monies - mayber remove this at some point
            matchControl.clearRawDatabasesOfSummoner(summonerId)
            return 200
        }

        return 500 // we failed something
    }

    /**
     * Sync our summoner. This method triggers a 'sync' which pulls down all the matches that a summoner has not saved
     * previously
     */
    fun syncMatches(summonerId: String) : Int {

        // download all the matches that have not been processed.
        matchControl.downloadAndSaveMatchSummaries(summonerId)
        matchControl.fetchAndSaveMatchesForASummoner(summonerId, 0)
        // refine all our match data
        return 1
    }

    /**
     * Sync just our match summaries. This means that we can save time by just fetching the summaries, and then
     * trigger the save of
     */
    fun syncMatchSummaries(summonerId: String): Int {
        matchControl.downloadAndSaveMatchSummaries(summonerId)
        return 200
    }

    fun fetchSyncProgress(summonerId: String): SyncProgress {
       return matchControl.fetchSyncProgress(summonerId)
    }
}