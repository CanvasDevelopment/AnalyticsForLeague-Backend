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

        // refine all our match data
        matchControl.refineMatchData(summonerId, SOLO, TOP)
        matchControl.refineMatchData(summonerId, SOLO, MID)
        matchControl.refineMatchData(summonerId, NONE, JUNGLE)
        matchControl.refineMatchData(summonerId, DUO_SUPPORT, BOT)
        matchControl.refineMatchData(summonerId, DUO_CARRY, BOT)

        // clear the raw database
        matchControl.clearRawDatabasesOfSummoner(summonerId)

        return 200 // TODO make this return a json object with the results for each role.
    }
}