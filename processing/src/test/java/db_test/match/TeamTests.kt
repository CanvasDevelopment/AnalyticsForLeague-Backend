package db_test.match

import db.DBHelper
import db.match.BanDAO
import db.match.TeamDAO
import model.match.Ban
import model.match.Team
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import java.util.*
import kotlin.collections.ArrayList

/**
 * @author Josiah Kendall
 */
class TeamTests {
    lateinit var dbHelper : DBHelper
    lateinit var banDAO : BanDAO
    lateinit var teamDAO : TeamDAO

    @Before
    fun setUp() {
        dbHelper = DBHelper()
        dbHelper.connect()
        banDAO = BanDAO(dbHelper)
        teamDAO = TeamDAO(dbHelper, banDAO)
    }

    @After
    fun cleanUp() {
        dbHelper.executeSQLScript("Delete from team where gameId = -1")
        dbHelper.executeSQLScript("Delete from ban where teamId = -1")
        dbHelper.executeSQLScript("Delete from ban where teamId = -2")
    }
    @Test
    fun ensureWeCanSaveAndLoadTeamAndBans() {
        val random = Random()
        val ban1 = Ban(random.nextInt(), random.nextInt())
        val ban2 = Ban(random.nextInt(), random.nextInt())
        val ban3 = Ban(random.nextInt(), random.nextInt())
        val ban4 = Ban(random.nextInt(), random.nextInt())
        val ban5 = Ban(random.nextInt(), random.nextInt())
        val bans = ArrayList<Ban>()
        bans.add(ban1)
        bans.add(ban2)
        bans.add(ban3)
        bans.add(ban4)
        bans.add(ban5)

        val team = Team(-1,
                "fail",
                false,
                false,
                false,
                false,
                false,
                false,
                1,
                2,
                3,
                random.nextInt(),
                random.nextInt(),
                random.nextInt(),
                0,
                bans)

        teamDAO.saveTeamForMatch(team, -1)
        val teamRetrieved = teamDAO.getTeamByGameIdAndTeamId(-1, -1)
        Assert.assertEquals(team.win, teamRetrieved.win)
        Assert.assertEquals(team.firstRiftHerald, teamRetrieved.firstRiftHerald)
        Assert.assertEquals(team.firstDragon, teamRetrieved.firstDragon)
        Assert.assertEquals(team.firstBaron, teamRetrieved.firstBaron)
        Assert.assertEquals(team.firstBlood, teamRetrieved.firstBlood)
        Assert.assertEquals(team.firstTower, teamRetrieved.firstTower)
        Assert.assertEquals(team.firstInhibitor, teamRetrieved.firstInhibitor)
        Assert.assertEquals(team.riftHeraldKills, teamRetrieved.riftHeraldKills)
        Assert.assertEquals(team.firstRiftHerald, teamRetrieved.firstRiftHerald)
        Assert.assertEquals(team.dragonKills, teamRetrieved.dragonKills)
        Assert.assertEquals(team.baronKills, teamRetrieved.baronKills)
        Assert.assertEquals(team.vileMawKills, teamRetrieved.vileMawKills)
        Assert.assertEquals(team.bans.size, teamRetrieved.bans.size)
        Assert.assertEquals(team.bans[0].championId, teamRetrieved.bans[0].championId)
        Assert.assertEquals(team.bans[0].pickTurn, teamRetrieved.bans[0].pickTurn)
        Assert.assertEquals(team.bans[1].championId, teamRetrieved.bans[1].championId)
        Assert.assertEquals(team.bans[1].pickTurn, teamRetrieved.bans[1].pickTurn)
        Assert.assertEquals(team.bans[2].championId, teamRetrieved.bans[2].championId)
        Assert.assertEquals(team.bans[2].pickTurn, teamRetrieved.bans[2].pickTurn)
        Assert.assertEquals(team.bans[3].championId, teamRetrieved.bans[3].championId)
        Assert.assertEquals(team.bans[3].pickTurn, teamRetrieved.bans[3].pickTurn)
        Assert.assertEquals(team.bans[4].championId, teamRetrieved.bans[4].championId)
        Assert.assertEquals(team.bans[4].pickTurn, teamRetrieved.bans[4].pickTurn)
    }

    @Test
    fun testThatWeCanGetBothTeamsForAGame() {
        val random = Random()
        val ban1 = Ban(random.nextInt(), random.nextInt())
        val ban2 = Ban(random.nextInt(), random.nextInt())
        val ban3 = Ban(random.nextInt(), random.nextInt())
        val ban4 = Ban(random.nextInt(), random.nextInt())
        val ban5 = Ban(random.nextInt(), random.nextInt())
        val bans = ArrayList<Ban>()
        bans.add(ban1)
        bans.add(ban2)
        bans.add(ban3)
        bans.add(ban4)
        bans.add(ban5)

        val team = Team(-1,
                "fail",
                false,
                false,
                false,
                false,
                false,
                false,
                1,
                2,
                3,
                random.nextInt(),
                random.nextInt(),
                random.nextInt(),
                0,
                bans)

        val team2 = Team(-2,
                "fail",
                false,
                false,
                false,
                false,
                false,
                false,
                1,
                2,
                3,
                random.nextInt(),
                random.nextInt(),
                random.nextInt(),
                0,
                bans)

        teamDAO.saveTeamForMatch(team, -1)
        teamDAO.saveTeamForMatch(team2, -1)

        val teams = teamDAO.getAllTeamsForGameId(-1)
        Assert.assertTrue(teams.size ==2)
    }
}