package db_test.match

import db.DBHelper
import db.match.BanDAO
import model.match.Ban
import org.junit.Assert
import org.junit.Before
import org.junit.Test

/**
 * @author Josiah Kendall
 */
class BanTests {
    lateinit var dbHelper : DBHelper
    lateinit var banDAO : BanDAO

    @Before
    fun doSetUp() {
        dbHelper = DBHelper()
        dbHelper.connect()
        banDAO = BanDAO(dbHelper)
    }

    @Test
    fun ensureThatWeCanSaveAndFetchBan() {
        val ban = Ban(34, 2)
        val banId = banDAO.saveBan(ban, -1)
        val banRecovered = banDAO.getBanById(banId)
        Assert.assertEquals(ban.pickTurn, banRecovered.pickTurn)
        Assert.assertEquals(ban.championId, banRecovered.championId)
    }

    @Test
    fun ensureThatWeCanFetchBanByTeamId() {
        val ban = Ban(34, 2)
        val banRecovered = banDAO.getBanByTeamRowId(-1)
        Assert.assertEquals(ban.pickTurn, banRecovered.pickTurn)
        Assert.assertEquals(ban.championId, banRecovered.championId)
    }
}