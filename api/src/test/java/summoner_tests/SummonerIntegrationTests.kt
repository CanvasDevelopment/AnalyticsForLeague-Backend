package summoner_tests

import api.controllers.SummonerController
import com.google.gson.Gson
import database.DbHelper
import db.summoner.SummonerDao
import model.response_beans.SummonerDetails
import model.response_beans.SummonerExistence
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import service_contracts.ProcessingContract
import java.util.*

/**
 * @author Josiah Kendall
 */
class SummonerIntegrationTests {

    private val SUMMONER = "Summoner"
    lateinit var summoner: SummonerController
    val gson = Gson()
    private val dbHelper = DbHelper()


    private lateinit var summonerDAO : SummonerDao
    @Before
    fun setUp() {
        dbHelper.connect()
        summonerDAO = SummonerDao(dbHelper)
        summoner = SummonerController(summonerDAO, mock(ProcessingContract::class.java))
    }

    @After
    fun cleanUp() {
        dbHelper.connect()
        dbHelper.executeSQLScript("DELETE FROM $SUMMONER")
    }

    @Test
    fun `Make sure that we can correctly check if a summoner exists`() {
        val summonerDetails = produceRandomSummoner()
        val firstCheck = summoner.isSummonerRegistered(summonerDetails.name)

        assert(!firstCheck.data)
        saveSummoner(summonerDetails)
        val secondCheck = summoner.isSummonerRegistered(summonerDetails.name)
        assert(secondCheck.data)
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