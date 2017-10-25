package domain_tests

import application.domain.SummonerControl
import db.summoner.SummonerDAOContractImpl
import model.Summoner
import model.networking.NetworkResult
import network.riotapi.SummonerService
import network.riotapi.SummonerServiceImpl
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.mockito.Matchers
import org.mockito.Mockito.*
import java.util.*
import kotlin.test.assertTrue

/**
 * @author Josiah Kendall
 */
class SummonerControlTests {

    val random = Random()

    lateinit var summonerService : SummonerServiceImpl
    lateinit var summonerDao : SummonerDAOContractImpl
    lateinit var summonerControl: SummonerControl

    @Before
    fun setup() {
        summonerDao = mock(SummonerDAOContractImpl::class.java)
        summonerService = mock(SummonerServiceImpl::class.java)
        summonerControl = SummonerControl(summonerDao,summonerService)
    }

    @Test
    fun `test that checkSummonerExists() returns false if summoner does not exist`() {
        `when`(summonerDao.getSummoner(Matchers.anyString())).thenReturn(null)
        val result = summonerControl.checkSummonerExists("")
        Assert.assertEquals(result, false)
    }

    @Test
    fun `test that checkSummonerExists() returns true if summoner does exist`() {
        `when`(summonerDao.getSummoner(Matchers.anyString())).thenReturn(Summoner())
        val result = summonerControl.checkSummonerExists("")
        Assert.assertEquals(result, true)
    }

    @Test
    fun `test that we return false if we try to register summoner and do not find a summoner`() {
        val networkResult = NetworkResult<Summoner>(null, 404)
        `when`(summonerService.fetchSummonerBySummonerName(Matchers.anyString(), Matchers.anyString())).thenReturn(networkResult)
        val registerdSuccessfully = summonerControl.registerSummoner("")
        assert(!registerdSuccessfully)
    }

    @Test
    fun `test we try save summoner when we get result`() {
        val summoner = Summoner()
        summoner.id = random.nextLong()
        val networkResult = NetworkResult(summoner, 200)
        `when`(summonerService.fetchSummonerBySummonerName(Matchers.anyString(), Matchers.anyString())).thenReturn(networkResult)
        `when`(summonerDao.getSummoner(summoner.id)).thenReturn(null)
        val registerdSuccessfully = summonerControl.registerSummoner("")
        verify(summonerDao, times(1)).saveSummoner(summoner)
    }

    @Test
    fun `test we dont save summoner that already exists`() {
        val summoner = Summoner()
        summoner.id = random.nextLong()
        val networkResult = NetworkResult(summoner, 200)
        `when`(summonerService.fetchSummonerBySummonerName(Matchers.anyString(), Matchers.anyString())).thenReturn(networkResult)
        `when`(summonerDao.getSummoner(Matchers.anyLong())).thenReturn(summoner)
        val registerdSuccessfully = summonerControl.registerSummoner("")
        verify(summonerDao, times(0)).saveSummoner(summoner)
    }

    @Test
    fun `test we can return true if we successfully save summoner`() {
        val summoner = Summoner()
        summoner.id = random.nextLong()
        val networkResult = NetworkResult(summoner, 200)
        `when`(summonerService.fetchSummonerBySummonerName(Matchers.anyString(), Matchers.anyString())).thenReturn(networkResult)
        `when`(summonerDao.getSummoner(Matchers.anyLong())).thenReturn(summoner)
        `when`(summonerDao.saveSummoner(summoner)).thenReturn(2)
        val registerdSuccessfully = summonerControl.registerSummoner("")
        assertTrue(registerdSuccessfully)
    }

    @Test
    fun `test we return false when we fail to save summoner`() {
        val summoner = Summoner()
        summoner.id = random.nextLong()
        val networkResult = NetworkResult(summoner, 200)
        `when`(summonerService.fetchSummonerBySummonerName(Matchers.anyString(), Matchers.anyString())).thenReturn(networkResult)
        `when`(summonerDao.getSummoner(Matchers.anyLong())).thenReturn(null)
        `when`(summonerDao.saveSummoner(summoner)).thenReturn(-1)
        val registerdSuccessfully = summonerControl.registerSummoner("")
        assertTrue(!registerdSuccessfully)
    }


//
}