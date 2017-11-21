package summoner_tests

import database.DbHelper
import db.summoner.SummonerDao
import model.response_beans.SummonerDetails
import org.junit.Assert
import org.junit.Test
import kotlin.test.assert

/**
 * @author Josiah Kendall
 */
class SummonerDaoTests {

    @Test
    fun `Make sure that we can find summoners that exist`() {
        val dao = SummonerDao (DbHelper())
        // make sure that the name exists
        val name = "88yogibear88"
        val playerDetails = SummonerDetails(123,456,name, 1, 3, 1234567)
        dao.saveSummoner(playerDetails)
        val summonerExists = dao.getSummoner(name) != null
        assert(summonerExists)
    }
}