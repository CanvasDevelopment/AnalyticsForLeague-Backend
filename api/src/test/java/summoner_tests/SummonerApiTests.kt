package summoner_tests

import api.SummonerApi
import database.DbHelper
import db.summoner.SummonerDao
import model.SummonerDetails
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.util.*

/**
 * @author Josiah Kendall
 */
class SummonerApiTests {

    private val SUMMONER = "Summoner"
    lateinit var summonerApi : SummonerApi
    private val dbHelper = DbHelper()
    private lateinit var summonerDAO : SummonerDao
    @Before
    fun setUp() {
        dbHelper.connect()
        summonerDAO = SummonerDao(dbHelper)
        summonerApi = SummonerApi(dbHelper, summonerDAO)
    }

    @After
    fun cleanUp() {
        dbHelper.connect()
        dbHelper.executeSQLScript("DELETE FROM $SUMMONER")
    }

    @Test
    fun `Make sure that we can correctly check if a summoner exists`() {
        val summonerDetails = produceRandomSummoner()
        val firstCheck = summonerApi.isSummonerRegistered(summonerDetails.name)
        assert(!firstCheck)
        saveSummoner(summonerDetails)
        val secondCheck = summonerApi.isSummonerRegistered(summonerDetails.name)
        assert(secondCheck)

    }

    private fun produceRandomSummoner() : SummonerDetails {
        val random = Random()
        return SummonerDetails(
                random.nextInt().toLong(),
                random.nextLong(),
                "test",
                random.nextInt(),
                random.nextInt(),
                random.nextLong())
    }

    private fun saveSummoner(summonerDeets : SummonerDetails) {
        val sql = "insert into summoner(" +
                "Id, " +
                "AccountId, " +
                "SummonerName, " +
                "ProfileIconId, " +
                "SummonerLevel, " +
                "revisionDate) VALUES (" +
                "${summonerDeets.id}," +
                "${summonerDeets.accountId}," +
                "'${summonerDeets.name}'," +
                "${summonerDeets.profileIconId}," +
                "${summonerDeets.summonerLevel}," +
                "${summonerDeets.revisionDate})"
        dbHelper.executeSQLScript(sql)
    }


}