package network_tests

import network.NetworkInterface
import network.riotapi.MatchService
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import util.RIOT_API_KEY

/**
 * @author Josiah Kendall
 */
class MatchServiceTests {

    lateinit var networkInterface : NetworkInterface
    lateinit var matchService : MatchService

    @Before
    fun setUp() {
        networkInterface = NetworkInterface("oc1")
        matchService = networkInterface.getMatchService()
    }

    @Test
    fun testThatWeCanFetchMatchList() {
        val matchList = matchService.getMatchListForAccount(RIOT_API_KEY, 200774483)
        Assert.assertTrue(matchList.startIndex == 0)
        Assert.assertTrue(matchList.endIndex == 732)
        Assert.assertTrue(matchList.matches[0].lane == "JUNGLE")
        Assert.assertEquals(matchList.matches[0].gameId, 161736353)
    }

    @Test
    fun testThatWeCanLoadMatchDetals() {
        val match = matchService.getMatchByMatchId(RIOT_API_KEY, 161736353)
        Assert.assertEquals(match.platformId, "OC1")
        Assert.assertEquals(match.queueId, 440)
        Assert.assertEquals(match.seasonId, 9)
        Assert.assertEquals(match.gameDuration, 2045)
        Assert.assertEquals(match.gameCreation, 1485213569711)
        Assert.assertEquals(match.gameVersion, "7.1.172.6393")
        Assert.assertEquals(match.gameMode, "CLASSIC")
        Assert.assertEquals(match.gameType, "MATCHED_GAME")
        Assert.assertEquals(match.teams.size, 2)
        Assert.assertEquals(match.participants.size, 10)
        Assert.assertEquals(match.participants[0].stats.firstTowerAssist, false)
        Assert.assertEquals(match.participantIdentities.size, 10)
        Assert.assertEquals(match.participantIdentities[0].participantId, 1)
        Assert.assertEquals(match.participants[0].timeline.creepsPerMinDeltas.zeroToTen, 6.0, 0.005)



    }
}