package database.summoner

import model.response_beans.SummonerDetails

/**
 * @author Josiah Kendall
 */
interface SummonerDAOContract {

    fun saveSummoner(summoner: SummonerDetails): Long
    fun deleteSummoner(summonerId : String)
    fun getSummoner(summonerId : String): SummonerDetails?

    companion object {
        val SUMMONER_TABLE = "Summoner"
        val ID = "Id"
        val ACCOUNT_ID = "AccountId"
        val SUMMONER_NAME = "SummonerName"
        val SUMMONER_LEVEL = "SummonerLevel"
        val PROFILE_ICON_ID = "ProfileIconId"
        val REVISION_DATE = "RevisionDate"
    }
}
