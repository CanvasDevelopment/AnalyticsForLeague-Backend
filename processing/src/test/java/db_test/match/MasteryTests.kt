package db_test.match

import db.requests.DBHelper
import db.match.MasteryDAO
import model.match.Mastery
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test

/**
 * @author Josiah Kendall
 */
class MasteryTests {
    lateinit var dbHelper : DBHelper
    lateinit var masteryDAO : MasteryDAO

    @Before
    fun setUp() {
        dbHelper = DBHelper()
        dbHelper.connect()
        masteryDAO = MasteryDAO(dbHelper)
    }

    @After
    fun cleanUp() {
        masteryDAO.clearMasteriesForDefaultParticipant()
    }

    @Test
    fun ensureThatWeCanSaveAndFetchAMasteryByItsId() {
        val mastery = Mastery(1, 5)
        val result = masteryDAO.saveMastery(mastery, -1)

        val masteryRecovered = masteryDAO.getMastery(result)
        Assert.assertTrue(masteryRecovered.rank == mastery.rank)
        Assert.assertTrue(masteryRecovered.masteryId == mastery.masteryId)
    }

    @Test
    fun ensureThatWeCanFetchMutipleMasteriesByParticipantId() {
        val mastery1 = Mastery(1, 5)
        val mastery2 = Mastery(2, 5)
        val mastery3 = Mastery(3, 5)
        val mastery4 = Mastery(4, 5)
        val mastery5 = Mastery(5, 5)

        masteryDAO.saveMastery(mastery1, -1)
        masteryDAO.saveMastery(mastery2, -1)
        masteryDAO.saveMastery(mastery3, -1)
        masteryDAO.saveMastery(mastery4, -1)
        masteryDAO.saveMastery(mastery5, -1)

        val masteries = masteryDAO.getAllMasteriesForParticipantInstance(-1)
        Assert.assertTrue(masteries.size == 5)
        Assert.assertTrue(masteries.get(0).masteryId == mastery1.masteryId)

    }
}