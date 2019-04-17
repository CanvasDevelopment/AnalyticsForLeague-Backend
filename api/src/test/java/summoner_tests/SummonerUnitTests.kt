package summoner_tests

import api.controllers.SummonerController
import com.google.gson.Gson
import db.summoner.SummonerDao
import model.response_beans.SummonerDetails
import model.response_beans.SummonerExistence
import org.junit.Test
import org.mockito.ArgumentMatchers
import org.mockito.Mockito.*
import service_contracts.ProcessingContract
import java.util.*

/**
 * @author Josiah Kendall
 */
class SummonerUnitTests {

    private val gson = Gson()
    private val random = Random()
    private val summonerDao = mock(SummonerDao::class.java)
    private val processor = mock(ProcessingContract::class.java)
    private val summonerController = SummonerController(summonerDao, processor)

    @Test
    fun `Make sure that we can register summoner correctly`() {
        val summonerDetails = SummonerDetails(random.nextLong().toString(),
                random.nextLong().toString(), "bob_the_feeder",
                random.nextInt(),
                random.nextInt(),
                random.nextLong())
        `when`(processor.createNewUser(ArgumentMatchers.anyString())).thenReturn(200)
        `when`(summonerDao.getSummoner(ArgumentMatchers.anyString())).thenReturn(summonerDetails)
        val savedSummonerResponse = summonerController.registerSummoner("bob_the_feeder")
        assert(savedSummonerResponse.data.id == summonerDetails.id)
        assert(savedSummonerResponse.data.accountId == summonerDetails.accountId)
        assert(savedSummonerResponse.data.revisionDate == summonerDetails.revisionDate)
        assert(savedSummonerResponse.data.name == summonerDetails.name)
        assert(savedSummonerResponse.data.profileIconId== summonerDetails.profileIconId)
        assert(savedSummonerResponse.data.summonerLevel== summonerDetails.summonerLevel)
    }

    @Test
    fun `Make sure that we return 404 when we fail to find the summoner given`() {
        val summonerName = "88yogibear88"
        `when`(processor.createNewUser(summonerName)).thenReturn(404)
        val savedSummonerResponse = summonerController.registerSummoner(summonerName)
        assert(savedSummonerResponse.resultCode == 404)
    }

    @Test
    fun `Make sure that we can return 404 successfully when looking for summoner`() {
        val summonerName = "88yogibear88"
        `when`(summonerDao.getSummoner(summonerName)).thenReturn(null)
        val summonerExistence = summonerController.isSummonerRegistered(summonerName)
        assert(summonerExistence.resultCode == 404)
        assert(!summonerExistence.data)
    }

    @Test
    fun `Make sure that we can return 404 if we fail to find the summoner when retrieving summoner details`() {
        val summonerId : String = "1"
        `when`(summonerDao.getSummoner(summonerId)).thenReturn(null)
        val response = summonerController.fetchSummonerDetails(summonerId)
        assert(response.resultCode == 404)
    }
}