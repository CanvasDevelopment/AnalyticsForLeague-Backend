package domain_tests

import application.domain.SummonerManagement
import db.summoner.SummonerDAOContractImpl
import model.Summoner
import model.networking.NetworkResult
import network.riotapi.SummonerService
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.mockito.Matchers
import org.mockito.Mockito.*
import kotlin.test.assertTrue

/**
 * @author Josiah Kendall
 */
class SummonerManagementTests {

    lateinit var summonerService : SummonerService
    lateinit var summonerDao : SummonerDAOContractImpl
    lateinit var summonerManagement : SummonerManagement

    @Before
    fun setup() {
        summonerDao = mock(SummonerDAOContractImpl::class.java)
        summonerService = mock(SummonerService::class.java)
        summonerManagement = SummonerManagement(summonerDao,summonerService)
    }

    @Test
    fun `test that checkSummonerExists() returns false if summoner does not exist`() {
        `when`(summonerDao.getSummoner(Matchers.anyString())).thenReturn(null)
        val result = summonerManagement.checkSummonerExists("")
        Assert.assertEquals(result, false)
    }

    @Test
    fun `test that checkSummonerExists() returns true if summoner does exist`() {
        `when`(summonerDao.getSummoner(Matchers.anyString())).thenReturn(Summoner())
        val result = summonerManagement.checkSummonerExists("")
        Assert.assertEquals(result, true)
    }

    @Test
    fun `test that we return false if we try to register summoner and do not find a summoner`() {
        val networkResult = NetworkResult<Summoner>(null, 404)
        `when`(summonerService.fetchSummonerBySummonerName(Matchers.anyString(), Matchers.anyString())).thenReturn(networkResult)
        val registerdSuccessfully = summonerManagement.registerSummoner("")
        assert(!registerdSuccessfully)
    }

    @Test
    fun `test we try save summoner when we get result`() {
        val networkResult = NetworkResult<Summoner>(Summoner(), 200)
        `when`(summonerService.fetchSummonerBySummonerName(Matchers.anyString(), Matchers.anyString())).thenReturn(networkResult)
        val registerdSuccessfully = summonerManagement.registerSummoner("")
        verify(summonerDao, times(1)).saveSummoner(any(Summoner::class.java))
    }

    @Test
    fun `test we dont save summoner that already exists`() {
        val networkResult = NetworkResult<Summoner>(Summoner(), 200)
        `when`(summonerService.fetchSummonerBySummonerName(Matchers.anyString(), Matchers.anyString())).thenReturn(networkResult)
        `when`(summonerDao.getSummoner(Matchers.anyLong())).thenReturn(Summoner())
        val registerdSuccessfully = summonerManagement.registerSummoner("")
        verify(summonerDao, times(0)).saveSummoner(Matchers.any(Summoner::class.java))
    }

    @Test
    fun `test we can return true if we successfully save summoner`() {
        val networkResult = NetworkResult<Summoner>(Summoner(), 200)
        `when`(summonerService.fetchSummonerBySummonerName(Matchers.anyString(), Matchers.anyString())).thenReturn(networkResult)
        `when`(summonerDao.getSummoner(Matchers.anyLong())).thenReturn(null)
        `when`(summonerDao.saveSummoner(Matchers.any(Summoner::class.java))).thenReturn(2)
        val registerdSuccessfully = summonerManagement.registerSummoner("")
        assertTrue(registerdSuccessfully)
    }

    @Test
    fun `test we return false when we fail to save summoner`() {
        val networkResult = NetworkResult<Summoner>(Summoner(), 200)
        `when`(summonerService.fetchSummonerBySummonerName(Matchers.anyString(), Matchers.anyString())).thenReturn(networkResult)
        `when`(summonerDao.getSummoner(Matchers.anyLong())).thenReturn(null)
        `when`(summonerDao.saveSummoner(Matchers.any(Summoner::class.java))).thenReturn(-1)
        val registerdSuccessfully = summonerManagement.registerSummoner("")
        assertTrue(!registerdSuccessfully)
    }


//
}