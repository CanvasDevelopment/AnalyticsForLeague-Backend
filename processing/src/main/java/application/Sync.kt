package application

import application.domain.MatchControl
import util.*

/**
 * @author Josiah Kendall
 */
class Sync (val matchControl: MatchControl){

    /**
     * Sync our summoner. This method triggers a 'sync' which pulls down all the matches that a summoner has not
     */
    fun syncMatches(summonerId : Long) {
        matchControl.downloadAndSaveMatches(summonerId)
        // refine all our match data
        matchControl.refineMatchData(summonerId, SOLO, TOP)
        matchControl.refineMatchData(summonerId, SOLO, MID)
        matchControl.refineMatchData(summonerId, NONE, JUNGLE)
        matchControl.refineMatchData(summonerId, DUO_SUPPORT, BOT)
        matchControl.refineMatchData(summonerId, DUO_CARRY, BOT)
    }
}