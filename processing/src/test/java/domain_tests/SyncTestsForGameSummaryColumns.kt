package domain_tests

import application.Sync
import application.domain.MatchControl
import com.github.salomonbrys.kodein.instance
import com.google.gson.Gson
import db.matchlist.MatchSummaryDAO
import db.summoner.SummonerDAOContract
import di.KodeinManager
import model.Summoner
import model.match.Match
import model.matchlist.MatchList
import network.riotapi.MatchServiceApi
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Test
import org.mockito.ArgumentMatchers
import org.mockito.Mockito.*
import java.io.BufferedReader
import java.io.FileInputStream
import java.io.InputStreamReader

/**
 * @author Josiah Kendall
 *
 * This class is to test that we fetch from the server and correctly save to the refined databases.
 * On set up, we grab 20 match files that are stored on disk, and parse them all to the databases. We then make sure that
 * they have been refined correctly.
 *
 * These are more integration tests than unit tests, however some mocks are present to reduce the dependencies on outside
 * factors that these tests need to run
 */
class SyncTestsForGameSummaryColumns {
    companion object {
        private val km = KodeinManager()
        private val gson = Gson()
        lateinit var sync: Sync
        private val matchServiceApi = mock(MatchServiceApi::class.java)

        private val match1Id: Long = 161340788
        private val match2Id: Long = 161342201
        private val match3Id: Long = 161342758
        private val match4Id: Long = 161345099
        private val match5Id: Long = 161345363
        private val match6Id: Long = 161345670
        private val match7Id: Long = 161346846
        private val match8Id: Long = 161351156
        private val match9Id: Long = 161351371
        private val match10Id: Long = 161355989
        private val match11Id: Long = 161356944
        private val match12Id: Long = 161413410
        private val match13Id: Long = 161415626
        private val match14Id: Long = 161487148
        private val match15Id: Long = 161487742
        private val match16Id: Long = 161600651
        private val match17Id: Long = 161607007
        private val match18Id: Long = 161613286
        private val match19Id: Long = 161667643
        private val match20Id: Long = 161736353

        @BeforeClass
        @JvmStatic
        fun setUp() {
            // need to mock the match service api, so that we just return results really fast.
            val matchList: MatchList = gson.fromJson(getMatchList(), MatchList::class.java)
            val match1 = gson.fromJson(getMatchString(match1Id), Match::class.java)
            `when`(matchServiceApi.getMatchByMatchId(anyString(), eq(match1Id))).thenReturn(match1)
            val match2 = gson.fromJson(getMatchString(match2Id), Match::class.java)
            `when`(matchServiceApi.getMatchByMatchId(anyString(), eq(match2Id))).thenReturn(match2)
            val match3 = gson.fromJson(getMatchString(match3Id), Match::class.java)
            `when`(matchServiceApi.getMatchByMatchId(anyString(), eq(match3Id))).thenReturn(match3)
            val match4 = gson.fromJson(getMatchString(match4Id), Match::class.java)
            `when`(matchServiceApi.getMatchByMatchId(anyString(), eq(match4Id))).thenReturn(match4)
            val match5 = gson.fromJson(getMatchString(match5Id), Match::class.java)
            `when`(matchServiceApi.getMatchByMatchId(anyString(), eq(match5Id))).thenReturn(match5)
            val match6 = gson.fromJson(getMatchString(match6Id), Match::class.java)
            `when`(matchServiceApi.getMatchByMatchId(anyString(), eq(match6Id))).thenReturn(match6)
            val match7 = gson.fromJson(getMatchString(match7Id), Match::class.java)
            `when`(matchServiceApi.getMatchByMatchId(anyString(), eq(match7Id))).thenReturn(match7)
            val match8 = gson.fromJson(getMatchString(match8Id), Match::class.java)
            `when`(matchServiceApi.getMatchByMatchId(anyString(), eq(match8Id))).thenReturn(match8)
            val match9 = gson.fromJson(getMatchString(match9Id), Match::class.java)
            `when`(matchServiceApi.getMatchByMatchId(anyString(), eq(match9Id))).thenReturn(match9)
            val match10 = gson.fromJson(getMatchString(match10Id), Match::class.java)
            `when`(matchServiceApi.getMatchByMatchId(anyString(), eq(match10Id))).thenReturn(match10)
            val match11 = gson.fromJson(getMatchString(match11Id), Match::class.java)
            `when`(matchServiceApi.getMatchByMatchId(anyString(), eq(match11Id))).thenReturn(match11)
            val match12 = gson.fromJson(getMatchString(match12Id), Match::class.java)
            `when`(matchServiceApi.getMatchByMatchId(anyString(), eq(match12Id))).thenReturn(match12)
            val match13 = gson.fromJson(getMatchString(match13Id), Match::class.java)
            `when`(matchServiceApi.getMatchByMatchId(anyString(), eq(match13Id))).thenReturn(match13)
            val match14 = gson.fromJson(getMatchString(match14Id), Match::class.java)
            `when`(matchServiceApi.getMatchByMatchId(anyString(), eq(match14Id))).thenReturn(match14)
            val match15 = gson.fromJson(getMatchString(match15Id), Match::class.java)
            `when`(matchServiceApi.getMatchByMatchId(anyString(), eq(match15Id))).thenReturn(match15)
            val match16 = gson.fromJson(getMatchString(match16Id), Match::class.java)
            `when`(matchServiceApi.getMatchByMatchId(anyString(), eq(match16Id))).thenReturn(match16)
            val match17 = gson.fromJson(getMatchString(match17Id), Match::class.java)
            `when`(matchServiceApi.getMatchByMatchId(anyString(), eq(match17Id))).thenReturn(match17)
            val match18 = gson.fromJson(getMatchString(match18Id), Match::class.java)
            `when`(matchServiceApi.getMatchByMatchId(anyString(), eq(match18Id))).thenReturn(match18)
            val match19 = gson.fromJson(getMatchString(match19Id), Match::class.java)
            `when`(matchServiceApi.getMatchByMatchId(anyString(), eq(match19Id))).thenReturn(match19)
            val match20 = gson.fromJson(getMatchString(match20Id), Match::class.java)
            `when`(matchServiceApi.getMatchByMatchId(anyString(), eq(match20Id))).thenReturn(match20)
            `when`(matchServiceApi.getMatchListForAccount(anyString(), anyLong())).thenReturn(matchList)

            val summoner = Summoner()
            summoner.accountId = 1234567
            val summonerDAOmock = mock(SummonerDAOContract::class.java)
            val matchSummaryDaoMock = mock(MatchSummaryDAO::class.java)
            `when`(summonerDAOmock.getSummoner(ArgumentMatchers.anyLong())).thenReturn(summoner)
            val matchControl = MatchControl(km.kodein.instance(),
                    matchServiceApi,
                    matchSummaryDaoMock,
                    summonerDAOmock,
                    km.kodein.instance(),
                    km.kodein.instance())
            sync = Sync(matchControl)
            sync.syncMatches(1542360)
        }

        @JvmStatic
        private fun getMatchString(matchId: Long): String {
            val inputs = FileInputStream("C:\\Users\\jek40\\Desktop\\analyticsforleague.com\\afl_backend\\processing\\src\\main\\resources\\json\\$matchId.json")
            val buf = BufferedReader(InputStreamReader(inputs))
            var line = buf.readLine()
            val sb = StringBuilder()
            while (line != null) {
                sb.append(line).append("\n")
                line = buf.readLine()
            }

            return sb.toString()
        }

        @JvmStatic
        private fun getMatchList(): String {

            val inputs = FileInputStream("C:\\Users\\jek40\\Desktop\\analyticsforleague.com\\afl_backend\\processing\\src\\main\\resources\\json\\recent.json")
            val buf = BufferedReader(InputStreamReader(inputs))
            var line = buf.readLine()
            val sb = StringBuilder()
            while (line != null) {
                sb.append(line).append("\n")
                line = buf.readLine()
            }

            return sb.toString()
        }
    }


}