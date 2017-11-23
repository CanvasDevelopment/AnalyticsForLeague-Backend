package model.match

/**
 * @author Josiah Kendall
 */
class Ban {
    var championId: Int = 0
    var pickTurn: Int = 0

    constructor(championId: Int, pickTurn: Int) {
        this.championId = championId
        this.pickTurn = pickTurn
    }

    constructor()

    operator fun component1() : Int = championId

    operator fun component2() : Int = pickTurn
}