package db_test.match

import db.DBHelper
import db.match.MasteryDAO
import db.match.RuneDAO
import model.match.Rune
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test

/**
 * @author Josiah Kendall
 */
class Rune_Tests {

    lateinit var dbHelper : DBHelper
    lateinit var runeDAO : RuneDAO

    @Before
    fun setUp() {
        dbHelper = DBHelper()
        dbHelper.connect()
        runeDAO = RuneDAO(dbHelper)
    }

    @After
    fun cleanUp() {
        runeDAO.clearRunesForDefaultParticipant()
    }

    @Test
    fun ensureThatWeSaveAndLoadARune() {
        val rune = Rune(1, 5)
        val result = runeDAO.saveRune(rune, -1)
        val rune2 = runeDAO.getRuneById(result)
        Assert.assertTrue(rune.rank == rune2.rank)
        Assert.assertTrue(rune.runeId == rune2.runeId)
    }

    @Test
    fun ensureThatWeCanFetchAllRunesForAParticipantRow() {
        val rune1 = Rune(1, 5)
        val rune2 = Rune(2, 5)
        val rune3 = Rune(3, 5)
        val rune4 = Rune(4, 5)
        val rune5 = Rune(5, 5)

        runeDAO.saveRune(rune1, -1)
        runeDAO.saveRune(rune2, -1)
        runeDAO.saveRune(rune3, -1)
        runeDAO.saveRune(rune4, -1)
        runeDAO.saveRune(rune5, -1)

        val runes = runeDAO.getAllRunesForAParticipantId(-1)
        Assert.assertTrue(runes.size == 5)

        Assert.assertTrue(runes.get(0).runeId == rune1.runeId)
        Assert.assertTrue(runes.get(1).runeId == rune2.runeId)
        Assert.assertTrue(runes.get(2).runeId == rune3.runeId)
        Assert.assertTrue(runes.get(3).runeId == rune4.runeId)
        Assert.assertTrue(runes.get(4).runeId == rune5.runeId)
    }
}