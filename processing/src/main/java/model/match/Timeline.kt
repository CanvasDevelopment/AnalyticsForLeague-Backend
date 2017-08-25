package model.match

import model.stats.GameStageDelta

/**
 * @author Josiah Kendall
 */
data class Timeline(val participantId : Int,
                    val creepsPerMinDeltas: GameStageDelta,
                    val xpPerMinDeltas: GameStageDelta,
                    val goldPerMinDeltas: GameStageDelta,
                    val csDiffPerMinDeltas: GameStageDelta,
                    val xpDiffPerMinDeltas: GameStageDelta,
                    val damageTakenPerMinDeltas: GameStageDelta,
                    val damageTakenDiffPerMinDeltas: GameStageDelta,
                    val role : String,
                    val lane : String)