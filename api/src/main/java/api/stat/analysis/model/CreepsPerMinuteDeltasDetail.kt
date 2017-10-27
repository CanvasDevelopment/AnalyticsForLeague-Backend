package api.stat.analysis.model

/**
 * @author Josiah Kendall
 */
class CreepsPerMinuteDeltasDetail(val creepsMaxHero : RawDelta,
                                  val creepsMaxEnemy : RawDelta,
                                  val creepsAverageHero : RawDelta,
                                  val creepsAverageEnemy : RawDelta,
                                  val creepsMinHero : RawDelta,
                                  val creepsMinEnemy : RawDelta)