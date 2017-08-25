package db_test.match

import com.github.salomonbrys.kodein.instance
import db.DBHelper
import db.match.BanDAO
import di.KodeinManager
import model.match.Ban
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import java.util.*

/**
 * @author Josiah Kendall
 */
class BanTests {
    lateinit var dbHelper : DBHelper
    lateinit var banDAO : BanDAO

    @Before
    fun doSetUp() {
        val km = KodeinManager()
        dbHelper = km.kodein.instance()
        banDAO = BanDAO(km.kodein.instance())
    }

    @After
    fun doCleanUp() {
        dbHelper.connect()
        dbHelper.executeSQLScript("Delete from ban where teamId = -1") // called team id not team row id.. todo change this
        dbHelper.disconnect()
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
        val random = Random()
        val ban1 = Ban(random.nextInt(), random.nextInt())
        val ban2 = Ban(random.nextInt(), random.nextInt())
        val ban3 = Ban(random.nextInt(), random.nextInt())
        val ban4 = Ban(random.nextInt(), random.nextInt())
        val ban5 = Ban(random.nextInt(), random.nextInt())
        banDAO.saveBan(ban1, -1)
        banDAO.saveBan(ban2,-1)
        banDAO.saveBan(ban3,-1)
        banDAO.saveBan(ban4,-1)
        banDAO.saveBan(ban5,-1)
        val bansRecovered = banDAO.getAllBansByTeamRowId(-1)
        Assert.assertTrue(bansRecovered.size == 5)
        var index = 0
        for ((championId, pickTurn) in bansRecovered) {
            Assert.assertEquals(pickTurn, bansRecovered[index].pickTurn)
            Assert.assertEquals(championId, bansRecovered[index].championId)
            index += 1
        }

    }
}