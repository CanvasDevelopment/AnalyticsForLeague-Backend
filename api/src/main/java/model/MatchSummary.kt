package model

/**
 * @author Josiah Kendall
 */
class MatchSummary(val earlyGameHero : Float,
                   val earlyGameVillan : Float,
                   val midGameHero : Float,
                   val midGameVillan : Float,
                   val lateGameHero : Float,
                   val lategameVillan : Float,
                   val won : Boolean,
                   val champId : Int)