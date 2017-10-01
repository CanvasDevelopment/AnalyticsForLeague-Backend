package db.summoner


import model.Summoner

/**
 * @author Josiah Kendall
 */
interface SummonerDAOContract {

    fun saveSummoner(summoner: Summoner): Long
    fun deleteSummoner(summonerId: Long)
    fun getSummoner(summonerId: Long): Summoner?

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
