package application

import application.domain.MatchControl
import util.*

/**
 * @author Josiah Kendall
 */
class Sync (val matchControl: MatchControl) {


    /**
     * Sync our summoner. This method triggers a 'sync' which pulls down all the matches that a summoner has not saved
     * previously
     */
    fun syncMatches(summonerId : Long) : Int {

        // download all the matches that have not been processed.
        matchControl.downloadAndSaveMatchSummaries(summonerId)
        matchControl.fetchAndSaveMatchesForASummoner(summonerId, 20)
        // refine all our match data
        val savedTop = matchControl.refineMatchData(summonerId, SOLO, TOP)
        val savedMid = matchControl.refineMatchData(summonerId, SOLO, MID)
        val savedJungle = matchControl.refineMatchData(summonerId, NONE, JUNGLE)
        val savedSup = matchControl.refineMatchData(summonerId, DUO_SUPPORT, BOT)
        val savedAdc = matchControl.refineMatchData(summonerId, DUO_CARRY, BOT)


        // Record the sync result here.

        // clear the raw database
        matchControl.clearRawDatabasesOfSummoner(summonerId)

        if (savedJungle &&  savedAdc && savedMid && savedSup && savedTop) {
            return 200
        }

        return 500 // we failed something
    }

    /**
     * Sync just our match summaries. This means that we can save time by just fetching the summaries, and then
     * trigger the save of
     */
    fun syncMatchSummaries(summonerId: Long): Int {
        matchControl.downloadAndSaveMatchSummaries(summonerId)
        return 200
    }
}