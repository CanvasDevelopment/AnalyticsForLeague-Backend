package model.match

import java.util.*

/**
 * @author Josiah Kendall
 */
class Team {
    var teamId: Int = 0
    var win: String = ""
    var firstBlood: Boolean = false
    var firstTower: Boolean = false
    var firstInhibitor: Boolean = false
    var firstBaron: Boolean = false
    var firstDragon: Boolean = false
    var firstRiftHerald: Boolean = false
    var towerKills: Int = 0
    var inhibitorKills: Int = 0
    var baronKills: Int = 0
    var dragonKills: Int = 0
    var vileMawKills: Int = 0
    var riftHeraldKills: Int = 0
    var dominionVictoryScore: Int = 0
    var bans: ArrayList<Ban> = ArrayList()

    constructor()

    constructor(teamId: Int,
                win: String,
                firstBlood: Boolean,
                firstTower: Boolean,
                firstInhibitor: Boolean,
                firstBaron: Boolean,
                firstDragon: Boolean,
                firstRiftHerald: Boolean,
                towerKills: Int,
                inhibitorKills: Int,
                baronKills: Int,
                dragonKills: Int,
                vileMawKills: Int,
                riftHeraldKills: Int,
                dominionVictoryScore: Int,
                bans: ArrayList<Ban>) {
        this.teamId = teamId
        this.win = win
        this.firstBlood = firstBlood
        this.firstTower = firstTower
        this.firstInhibitor = firstInhibitor
        this.firstBaron = firstBaron
        this.firstDragon = firstDragon
        this.firstRiftHerald = firstRiftHerald
        this.towerKills = towerKills
        this.inhibitorKills = inhibitorKills
        this.baronKills = baronKills
        this.dragonKills = dragonKills
        this.vileMawKills = vileMawKills
        this.riftHeraldKills = riftHeraldKills
        this.dominionVictoryScore = dominionVictoryScore
        this.bans = bans
    }
}