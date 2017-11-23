package model.match

import java.util.*

/**
 * @author Josiah Kendall
 */
class Participant {
    var participantId: Int = 0
    var teamId: Int = 0
    var championId: Int = 0
    var spell1Id: Int = 0
    var spell2Id: Int = 0
    var masteries: ArrayList<Mastery> = ArrayList()
    var runes: ArrayList<Rune> = ArrayList()
    var highestAchievedSeasonTier: String = ""
    var stats: Stats = Stats()
    var timeline: Timeline = Timeline()

    constructor(participantId: Int, teamId: Int, championId: Int, spell1Id: Int, spell2Id: Int, masteries: ArrayList<Mastery>, runes: ArrayList<Rune>, highestAchievedSeasonTier: String, stats: Stats, timeline: Timeline) {
        this.participantId = participantId
        this.teamId = teamId
        this.championId = championId
        this.spell1Id = spell1Id
        this.spell2Id = spell2Id
        this.masteries = masteries
        this.runes = runes
        this.highestAchievedSeasonTier = highestAchievedSeasonTier
        this.stats = stats
        this.timeline = timeline
    }
    constructor()
}