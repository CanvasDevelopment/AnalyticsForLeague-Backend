package model

/**
 * @author Josiah Kendall
 * *
 * * Pojo for a summoner.
 */
class Summoner {

    var id: Long = 0
    var accountId: Long = 0
    var name: String? = null
    var profileIconId: Int = 0
    var summonerLevel: Int = 0
    var revisionDate: Long = 0

    override fun toString(): String {
        val result = String.format("Id: %s\nAccountId: %s\nName: %s\nProfileIconId: %s\nSummonerLevel: %s\nRevisionDate: %s",
                id, accountId, name, profileIconId, summonerLevel, revisionDate)
        return result
    }
}
