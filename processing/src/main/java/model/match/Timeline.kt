package model.match

import model.stats.GameStageDelta

/**
 * @author Josiah Kendall
 */
class Timeline {
    var participantId: Int = 0
    var creepsPerMinDeltas: GameStageDelta = GameStageDelta()
    var xpPerMinDeltas: GameStageDelta  = GameStageDelta()
    var goldPerMinDeltas: GameStageDelta  = GameStageDelta()
    var csDiffPerMinDeltas: GameStageDelta  = GameStageDelta()
    var xpDiffPerMinDeltas: GameStageDelta  = GameStageDelta()
    var damageTakenPerMinDeltas: GameStageDelta  = GameStageDelta()
    var damageTakenDiffPerMinDeltas: GameStageDelta  = GameStageDelta()
    var role: String = ""
    var lane: String = ""

    constructor(participantId: Int, creepsPerMinDeltas: GameStageDelta, xpPerMinDeltas: GameStageDelta, goldPerMinDeltas: GameStageDelta, csDiffPerMinDeltas: GameStageDelta, xpDiffPerMinDeltas: GameStageDelta, damageTakenPerMinDeltas: GameStageDelta, damageTakenDiffPerMinDeltas: GameStageDelta, role: String, lane: String) {
        this.participantId = participantId
        this.creepsPerMinDeltas = creepsPerMinDeltas
        this.xpPerMinDeltas = xpPerMinDeltas
        this.goldPerMinDeltas = goldPerMinDeltas
        this.csDiffPerMinDeltas = csDiffPerMinDeltas
        this.xpDiffPerMinDeltas = xpDiffPerMinDeltas
        this.damageTakenPerMinDeltas = damageTakenPerMinDeltas
        this.damageTakenDiffPerMinDeltas = damageTakenDiffPerMinDeltas
        this.role = role
        this.lane = lane
    }

    constructor() {

    }
}