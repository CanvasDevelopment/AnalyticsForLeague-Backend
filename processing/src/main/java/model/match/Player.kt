package model.match

/**
 * @author Josiah Kendall
 */

class Player {
    var platformId: String = ""
    var accountId: Long = 0
    var summonerName: String = ""
    var summonerId: Long = 0
    var currentPlatformId: String = ""
    var matchHistoryUri: String = ""
    var profileIcon: Int = 0

    constructor(platformId: String, accountId: Long, summonerName: String, summonerId: Long, currentPlatformId: String, matchHistoryUri: String, profileIcon: Int) {
        this.platformId = platformId
        this.accountId = accountId
        this.summonerName = summonerName
        this.summonerId = summonerId
        this.currentPlatformId = currentPlatformId
        this.matchHistoryUri = matchHistoryUri
        this.profileIcon = profileIcon
    }

    constructor() {

    }
}
