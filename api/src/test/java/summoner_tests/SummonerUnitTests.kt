package summoner_tests

import api.controller.SummonerController
import com.google.gson.Gson
import database.DbHelper
import db.summoner.SummonerDao
import model.response_beans.SummonerDetails
import org.junit.Test
import org.mockito.ArgumentMatchers
import org.mockito.Mockito.*
import service_contracts.ProcessingContract
import java.util.*

/**
 * @author Josiah Kendall
 */
class SummonerUnitTests {

    val gson = Gson()
    val random = Random()
    private val summonerDao = mock(SummonerDao::class.java)
    private val dbHelper = mock(DbHelper::class.java)
    private val processor = mock(ProcessingContract::class.java)
    val summonerApi = SummonerController(dbHelper, summonerDao, processor)

    @Test
    fun `Make sure that we can register summoner correctly`() {
        val summonerDetails = SummonerDetails(random.nextLong(),
                random.nextLong(), "bob_the_feeder",
                random.nextInt(),
                random.nextInt(),
                random.nextLong())
        `when`(processor.createNewUser(ArgumentMatchers.anyString())).thenReturn(200)
        `when`(summonerDao.getSummoner(ArgumentMatchers.anyString())).thenReturn(summonerDetails)
        val savedSummonerResponse = summonerApi.registerSummoner("bob_the_feeder")
        val result = gson.fromJson<SummonerDetails>(savedSummonerResponse.data, SummonerDetails::class.java)
        assert(result.id == summonerDetails.id)
        assert(result.accountId == summonerDetails.accountId)
        assert(result.revisionDate == summonerDetails.revisionDate)
        assert(result.name == summonerDetails.name)
        assert(result.profileIconId== summonerDetails.profileIconId)
        assert(result.summonerLevel== summonerDetails.summonerLevel)
    }

    @Test
    fun `Make sure that we return -1 if the summoner was not correctly saved`() {

    }


}