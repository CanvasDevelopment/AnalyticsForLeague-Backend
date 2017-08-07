package model.match

import model.stats.GameStageDelta

/**
 * @author Josiah Kendall
 */
data class Timeline(val participantId : Int,
                    val creepsPerMin : GameStageDelta,
                    val xpPerMin : GameStageDelta,
                    val goldPerMin : GameStageDelta,
                    val csDiffPerMin : GameStageDelta,
                    val xpDiffPerMin: GameStageDelta,
                    val damageTakenPerMin : GameStageDelta,
                    val damageTakenDiffPerMin : GameStageDelta,
                    val role : String,
                    val lane : String)