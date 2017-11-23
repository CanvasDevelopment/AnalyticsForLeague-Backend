package model.match

/**
 * @author Josiah Kendall
 */
class Stats {
    var participantId: Int = 0
    var win: Boolean = false
    var item0: Int = 0
    var item1: Int = 0
    var item2: Int = 0
    var item3: Int = 0
    var item4: Int = 0
    var item5: Int = 0
    var item6: Int = 0
    var kills: Int = 0
    var deaths: Int = 0
    var assists: Int = 0
    var largestKillingSpree: Int = 0
    var largestMultiKill: Int = 0
    var killingSprees: Int = 0
    var longestTimeSpentLiving: Int = 0
    var doubleKills: Int = 0
    var tripleKills: Int = 0
    var quadraKills: Int = 0
    var pentaKills: Int = 0
    var unrealKills: Int = 0
    var totalDamageDealt: Int = 0
    var magicDamageDealt: Int = 0
    var physicalDamageDealt: Int = 0
    var trueDamageDealt: Int = 0
    var largestCriticalStrike: Int = 0
    var totalDamageDealtToChampions: Int = 0
    var magicDamageDealtToChampions: Int = 0
    var physicalDamageDealtToChampions: Int = 0
    var trueDamageDealtToChampions: Int = 0
    var totalHeal: Int = 0
    var totalUnitsHealed: Int = 0
    var damageSelfMitigated: Int = 0
    var damageDealtToObjectives: Int = 0
    var damageDealtToTurrets: Int = 0
    var visionScore: Int = 0
    var timeCCingOthers: Int = 0
    var totalDamageTaken: Int = 0
    var magicDamageTaken: Int = 0
    var physicalDamageTaken: Int = 0
    var trueDamageTaken: Int = 0
    var goldEarned: Int = 0
    var goldSpent: Int = 0
    var turretKills: Int = 0
    var inhibitorKills: Int = 0
    var totalMinionsKilled: Int = 0
    var neutralMinionsKilled: Int = 0
    var neutralMinionsKilledTeamJungle: Int = 0
    var neutralMinionsKilledEnemyJungle: Int = 0
    var totalTimeCrowdControlDealt: Int = 0
    var champLevel: Int = 0
    var visionWardsBoughtInGame: Int = 0
    var sightWardsBoughtInGame: Int = 0
    var wardsPlaced: Int = 0
    var wardsKilled: Int = 0
    var firstBloodKill: Boolean = false
    var firstBloodAssist: Boolean = false
    var firstTowerKill: Boolean = false
    var firstTowerAssist: Boolean = false
    var firstInhibitorKill: Boolean = false
    var firstInhibitorAssist: Boolean = false
    var combatPlayerScore: Int = 0
    var objectivePlayerScore: Int = 0
    var totalPlayerScore: Int = 0
    var totalScoreRank: Int = 0

    constructor(participantId: Int, win: Boolean, item0: Int, item1: Int, item2: Int, item3: Int, item4: Int, item5: Int, item6: Int, kills: Int, deaths: Int, assists: Int, largestKillingSpree: Int, largestMultiKill: Int, killingSprees: Int, longestTimeSpentLiving: Int, doubleKills: Int, tripleKills: Int, quadraKills: Int, pentaKills: Int, unrealKills: Int, totalDamageDealt: Int, magicDamageDealt: Int, physicalDamageDealt: Int, trueDamageDealt: Int, largestCriticalStrike: Int, totalDamageDealtToChampions: Int, magicDamageDealtToChampions: Int, physicalDamageDealtToChampions: Int, trueDamageDealtToChampions: Int, totalHeal: Int, totalUnitsHealed: Int, damageSelfMitigated: Int, damageDealtToObjectives: Int, damageDealtToTurrets: Int, visionScore: Int, timeCCingOthers: Int, totalDamageTaken: Int, magicDamageTaken: Int, physicalDamageTaken: Int, trueDamageTaken: Int, goldEarned: Int, goldSpent: Int, turretKills: Int, inhibitorKills: Int, totalMinionsKilled: Int, neutralMinionsKilled: Int, neutralMinionsKilledTeamJungle: Int, neutralMinionsKilledEnemyJungle: Int, totalTimeCrowdControlDealt: Int, champLevel: Int, visionWardsBoughtInGame: Int, sightWardsBoughtInGame: Int, wardsPlaced: Int, wardsKilled: Int, firstBloodKill: Boolean, firstBloodAssist: Boolean, firstTowerKill: Boolean, firstTowerAssist: Boolean, firstInhibitorKill: Boolean, firstInhibitorAssist: Boolean, combatPlayerScore: Int, objectivePlayerScore: Int, totalPlayerScore: Int, totalScoreRank: Int) {
        this.participantId = participantId
        this.win = win
        this.item0 = item0
        this.item1 = item1
        this.item2 = item2
        this.item3 = item3
        this.item4 = item4
        this.item5 = item5
        this.item6 = item6
        this.kills = kills
        this.deaths = deaths
        this.assists = assists
        this.largestKillingSpree = largestKillingSpree
        this.largestMultiKill = largestMultiKill
        this.killingSprees = killingSprees
        this.longestTimeSpentLiving = longestTimeSpentLiving
        this.doubleKills = doubleKills
        this.tripleKills = tripleKills
        this.quadraKills = quadraKills
        this.pentaKills = pentaKills
        this.unrealKills = unrealKills
        this.totalDamageDealt = totalDamageDealt
        this.magicDamageDealt = magicDamageDealt
        this.physicalDamageDealt = physicalDamageDealt
        this.trueDamageDealt = trueDamageDealt
        this.largestCriticalStrike = largestCriticalStrike
        this.totalDamageDealtToChampions = totalDamageDealtToChampions
        this.magicDamageDealtToChampions = magicDamageDealtToChampions
        this.physicalDamageDealtToChampions = physicalDamageDealtToChampions
        this.trueDamageDealtToChampions = trueDamageDealtToChampions
        this.totalHeal = totalHeal
        this.totalUnitsHealed = totalUnitsHealed
        this.damageSelfMitigated = damageSelfMitigated
        this.damageDealtToObjectives = damageDealtToObjectives
        this.damageDealtToTurrets = damageDealtToTurrets
        this.visionScore = visionScore
        this.timeCCingOthers = timeCCingOthers
        this.totalDamageTaken = totalDamageTaken
        this.magicDamageTaken = magicDamageTaken
        this.physicalDamageTaken = physicalDamageTaken
        this.trueDamageTaken = trueDamageTaken
        this.goldEarned = goldEarned
        this.goldSpent = goldSpent
        this.turretKills = turretKills
        this.inhibitorKills = inhibitorKills
        this.totalMinionsKilled = totalMinionsKilled
        this.neutralMinionsKilled = neutralMinionsKilled
        this.neutralMinionsKilledTeamJungle = neutralMinionsKilledTeamJungle
        this.neutralMinionsKilledEnemyJungle = neutralMinionsKilledEnemyJungle
        this.totalTimeCrowdControlDealt = totalTimeCrowdControlDealt
        this.champLevel = champLevel
        this.visionWardsBoughtInGame = visionWardsBoughtInGame
        this.sightWardsBoughtInGame = sightWardsBoughtInGame
        this.wardsPlaced = wardsPlaced
        this.wardsKilled = wardsKilled
        this.firstBloodKill = firstBloodKill
        this.firstBloodAssist = firstBloodAssist
        this.firstTowerKill = firstTowerKill
        this.firstTowerAssist = firstTowerAssist
        this.firstInhibitorKill = firstInhibitorKill
        this.firstInhibitorAssist = firstInhibitorAssist
        this.combatPlayerScore = combatPlayerScore
        this.objectivePlayerScore = objectivePlayerScore
        this.totalPlayerScore = totalPlayerScore
        this.totalScoreRank = totalScoreRank
    }
    // used by gson I think
    constructor() {

    }
}