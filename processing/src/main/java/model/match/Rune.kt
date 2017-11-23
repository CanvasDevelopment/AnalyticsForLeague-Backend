package model.match

/**
 * @author Josiah Kendall
 */
class Rune {
    var runeId = 0
    var rank = 0
    constructor() {

    }
    constructor(runeId : Int, rank : Int) {
        this.runeId = runeId
        this.rank = rank
    }
}