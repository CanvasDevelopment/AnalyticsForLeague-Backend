package db_test.match

import db.requests.DBHelper
import db.match.PlayerDAO
import model.match.Player
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test

/**
 * @author Josiah Kendall
 */
class PlayerTests {

    val dbHelper : DBHelper = DBHelper()

    @Before
    fun doSetUp() {
        dbHelper.connect()
    }

    @After
    fun doTearDown() {
        dbHelper.disconnect()
    }

    @Test
    fun ensure_WeCanSaveAndLoadPlayer() {
        val platformId = "oce"
        val accountId = 1234567
        val summonerName = "kloin"
        val summonerId = 1234598
        val currentPlatformId = "dfg"
        val matchHistoryId = "sdf"
        val profileIcon = 1
        val player = Player(
                platformId,
                1234567,
                "kloin",
                123489,
                "oce",
                "sdf",
                1)
        val playerDAO = PlayerDAO(dbHelper)

        val result = playerDAO.savePlayer(player, 3)
        val player2 = playerDAO.getPlayer(result)
        Assert.assertTrue(player2.matchHistoryUri == "sdf")
        Assert.assertTrue(player2.summonerName == player.summonerName)
        Assert.assertTrue(player2.profileIcon == player.profileIcon)
        Assert.assertTrue(player2.accountId == player.accountId)

    }
}