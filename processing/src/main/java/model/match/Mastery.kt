package model.match

/**
 * @author Josiah Kendall
 */
class Mastery {
    var masteryId : Int = 0
    var rank : Int = 0

    constructor(masteryId : Int, rank : Int) {
        this.masteryId = masteryId
        this.rank = rank
    }
}