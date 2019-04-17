package model

/**
 * @author Josiah Kendall
 * *
 * * Pojo for a summoner.
 */
class Summoner {

    var id: String = ""
    var accountId: String = ""
    var name: String? = ""
    var puuid : String = ""
    var profileIconId: Int = 0
    var summonerLevel: Int = 0
    var revisionDate: Long = 0

    override fun toString(): String {
        val result = String.format("Id: %s\nAccountId: %s\nName: %s\nProfileIconId: %s\nSummonerLevel: %s\nRevisionDate: %s",
                id, accountId, name, profileIconId, summonerLevel, revisionDate)
        return result
    }
}
