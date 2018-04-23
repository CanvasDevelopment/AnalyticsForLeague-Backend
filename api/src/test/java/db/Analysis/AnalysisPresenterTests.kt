package db.Analysis

import api.stat.analysis.AnalysisDao
import api.stat.analysis.AnalysisPresenter
import api.stat.analysis.model.HERO_CREEPS
import api.stat.analysis.model.RawDelta
import api.stat.analysis.model.VILLAN_CREEPS
import org.junit.Test
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import service_contracts.ProcessingImpl
import java.util.*

/**
 * @author Josiah Kendall
 */
class AnalysisPresenterTests {

    private val random = Random()

    private val processingApi = mock(ProcessingImpl::class.java)
    private val analysisDao = mock(AnalysisDao::class.java)
    private val analysisPresenter = AnalysisPresenter(processingApi, analysisDao)


    @Test
    fun `Make sure that we can grab card Creep stat details correctly`() {
        val summonerId = random.nextLong()
        val games = random.nextInt()
        val lane = "Top"
        val heroMax = produceRandomRawDelta()
        val villanMax = produceRandomRawDelta()
        val heroMin = produceRandomRawDelta()
        val heroAvg = produceRandomRawDelta()
        val villanMin = produceRandomRawDelta()
        val villanAvg = produceRandomRawDelta()


        `when`(analysisDao.fetchAvgDeltas(summonerId,games,lane, HERO_CREEPS)).thenReturn(heroAvg)
        `when`(analysisDao.fetchAvgDeltas(summonerId,games,lane, VILLAN_CREEPS)).thenReturn(villanAvg)
        `when`(analysisDao.fetchMaxDeltas(summonerId,games,lane, HERO_CREEPS)).thenReturn(heroMax)
        `when`(analysisDao.fetchMaxDeltas(summonerId,games,lane, VILLAN_CREEPS)).thenReturn(villanMax)
        `when`(analysisDao.fetchMinDeltas(summonerId,games,lane, HERO_CREEPS)).thenReturn(heroMin)
        `when`(analysisDao.fetchMinDeltas(summonerId,games,lane, VILLAN_CREEPS)).thenReturn(villanMin)

        val cardDetail = analysisPresenter.creepsPerMinuteDeltasDetail(summonerId,games, lane)

        assert(cardDetail.creepsAverageEnemy == villanAvg)
        assert(cardDetail.creepsMaxEnemy== villanMax)
        assert(cardDetail.creepsMinEnemy== villanMin)
        assert(cardDetail.creepsAverageHero== heroAvg)
        assert(cardDetail.creepsMaxHero== heroMax)
        assert(cardDetail.creepsMinHero== heroMin)
    }

    private fun produceRandomRawDelta() : RawDelta {
        return RawDelta(random.nextFloat(), random.nextFloat(), random.nextFloat())
    }
}