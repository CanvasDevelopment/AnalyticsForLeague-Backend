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
    constructor()

    /**
     * Create the value part of a sql insert string
     *
     * @param participantRowId The particpant row id. Needed in each rune row.
     */
    fun insertString(participantRowId: Long): String {
        return "($masteryId, $participantRowId, $rank)"
    }

}