package model.match

/**
 * @author Josiah Kendall
 */

class Player {
    var platformId: String = ""
    var accountId: String = ""
    var summonerName: String = ""
    var summonerId: String = ""
    var currentPlatformId: String = ""
    var matchHistoryUri: String = ""
    var profileIcon: Int = 0

    constructor(platformId: String, accountId: String, summonerName: String, summonerId: String, currentPlatformId: String, matchHistoryUri: String, profileIcon: Int) {
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
