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

    /**
     *
     */
    fun produceInsertStatement() : String {
       return "Insert "
    }

    /**
     * Create the value part of a sql insert string
     *
     * @param participantRowId The particpant row id. Needed in each rune row.
     */
    fun insertString(participantRowId: Long): String {
        return "($participantRowId, $runeId, $rank)"
    }
}