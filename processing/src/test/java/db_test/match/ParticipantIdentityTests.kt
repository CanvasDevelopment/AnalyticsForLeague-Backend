package db_test.match

import db.requests.DBHelper
import db.match.ParticipantIdentityDAO
import db.match.PlayerDAO
import model.match.ParticipantIdentity
import model.match.Player
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import java.util.*

/**
 * @author Josiah Kendall
 */
class ParticipantIdentityTests {

    lateinit var dbHelper : DBHelper
    lateinit var piDAO : ParticipantIdentityDAO
    lateinit var playerDAO : PlayerDAO

    val gameId : Long = -1
    @Before fun setUp() {
        dbHelper = DBHelper()
        dbHelper.connect()

        playerDAO = PlayerDAO(dbHelper)
        piDAO = ParticipantIdentityDAO(dbHelper, playerDAO)
    }

    @After fun finishUp() {
        dbHelper.executeSQLScript("Delete from participantIdentity where gameId = -1")
        dbHelper.executeSQLScript("Delete from player")
        dbHelper.disconnect()
    }

    fun produceRandomPlayer() : Player {
        val summonerId : String = Random().nextLong().toString()
        return Player(
                "OCE", // doesnt matter
                "556", // not used in current tests
                "Joe", // not used in current tests its just info
                summonerId, // used
                "OCE", // not used
                "BLAH", // not used
                1) // not used
    }

    @Test
    fun testWeGetRandomlyDifferentPlayers() {
        val player1 = produceRandomPlayer()
        val player2 = produceRandomPlayer()
        val player3 = produceRandomPlayer()

        Assert.assertTrue(player1.summonerId != player2.summonerId)
        Assert.assertTrue(player1.summonerId != player3.summonerId)
        Assert.assertTrue(player2.summonerId != player3.summonerId)
    }

    @Test
    fun EnsureWeCanSaveAndLoadParticipantEntityAndPlayer() {
        val player1 = produceRandomPlayer()
        val pi = ParticipantIdentity(1, gameId, player1)
        val id = piDAO.saveParticipantIdentity(pi, gameId, pi.player!!.summonerId, -1,"SOLO", "TOP")

        val pi2 = piDAO.getParticipantIdentity(gameId, player1.summonerId)
        Assert.assertTrue(pi.player?.summonerName == pi2.player!!.summonerName)
        Assert.assertTrue(pi.player?.platformId == pi2.player!!.platformId)
        Assert.assertTrue(pi.gameId == pi2.gameId)
        Assert.assertTrue(pi.participantId == pi2.participantId)
    }

    @Test
    fun EnsureWeCanSaveAndLoadParticipantIdentityAndCorrectPlayerWhenWeHaveMultiplePlayersInTheSameGame() {
        val player1 = produceRandomPlayer()
        val player2 = produceRandomPlayer()
        val player3 = produceRandomPlayer()

        val pi1 = ParticipantIdentity(1, gameId, player1)
        val pi2 = ParticipantIdentity(2, gameId, player2)
        val pi3 = ParticipantIdentity(3, gameId, player3)

        piDAO.saveParticipantIdentity(pi1, gameId, pi1.player!!.summonerId,-1,"SOLO", "TOP")
        piDAO.saveParticipantIdentity(pi2, gameId, pi2.player!!.summonerId,-1,"SOLO", "TOP")
        piDAO.saveParticipantIdentity(pi3, gameId, pi3.player!!.summonerId,-1,"SOLO", "TOP")

        val result = piDAO.getParticipantIdentity(pi2.gameId, pi2.player!!.summonerId)

        Assert.assertTrue(result.player!!.summonerName == pi2.player!!.summonerName)
        Assert.assertTrue(result.participantId == pi2.participantId)
    }

    @Test
    fun ensureWeCanLoadCorrectParticipantIdentityWhenWeHaveTheSameUserStoredOverMultipleGames() {

    }
}