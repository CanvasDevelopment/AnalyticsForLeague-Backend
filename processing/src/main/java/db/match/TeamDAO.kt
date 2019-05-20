package db.match

import db.requests.DBHelper
import extensions.ID
import extensions.produceTeam
import model.match.Team
import model.match.Match
import util.columnnames.TeamColumns
import java.util.*

/**
 * @author Josiah Kendall
 *
 * A Data Access Object for the [Team] table
 */
class TeamDAO(val dbHelper : DBHelper, val banDAO: BanDAO) {

    val teamColumns = TeamColumns()
    val TEAM_TABLE = "Team"

    /**
     * Save a [Team] for a [Match]
     * @param team The team to save
     * @param gameId The id of the match that we are saving
     */
    fun saveTeamForMatch(team : Team, gameId :Long) : Long {
        dbHelper.connect()
        val sql = "INSERT INTO $TEAM_TABLE(${teamColumns.TEAM_ID}, \n" +
                "                 ${teamColumns.WIN}, \n" +
                "                 ${teamColumns.FIRST_BLOOD}, \n" +
                "                 ${teamColumns.FIRST_TOWER}, \n" +
                "                 ${teamColumns.FIRST_INHIBITOR}, \n" +
                "                 ${teamColumns.FIRST_BARON}, \n" +
                "                 ${teamColumns.FIRST_DRAGON}, \n" +
                "                 ${teamColumns.FIRST_RIFT_HERALD}, \n" +
                "                 ${teamColumns.TOWER_KILLS}, \n" +
                "                 ${teamColumns.INHIBITOR_KILLS}, \n" +
                "                 ${teamColumns.BARON_KILLS}, \n" +
                "                 ${teamColumns.DRAGON_KILLS}, \n" +
                "                 ${teamColumns.VILE_MAW_KILLS}, \n" +
                "                 ${teamColumns.RIFT_HERALD_KILLS}, \n" +
                "                 ${teamColumns.DOMINION_VICOTORY_SCORE},\n" +
                "                 ${teamColumns.GAME_ID}) VALUES (" +
                "${team.teamId}," +
                "'${team.win}'," +
                "${team.firstBlood}," +
                "${team.firstTower}," +
                "${team.firstInhibitor}," +
                "${team.firstBaron}," +
                "${team.firstDragon}," +
                "${team.firstRiftHerald}," +
                "${team.towerKills}," +
                "${team.inhibitorKills}," +
                "${team.baronKills}," +
                "${team.dragonKills}," +
                "${team.vileMawKills}," +
                "${team.riftHeraldKills}," +
                "${team.dominionVictoryScore}," +
                "$gameId)"

        val teamId = dbHelper.executeSQLScript(sql)

        for(ban in team.bans) {
            banDAO.saveBan(ban, teamId)
        }
        return teamId
    }

    /**
     * Get a specific [Team] instance for a [Match]. As there are two teams, the [teamId] will always be 100 or 200
     * @param gameId The id of the match that we are interested in
     * @param teamId The id of the team in this specific match. Can be either 100 or 200.
     */
    fun getTeamByGameIdAndTeamId(gameId : Long, teamId : Int) : Team {
        dbHelper.connect()
        val sql = "SELECT * FROM $TEAM_TABLE " +
                "WHERE ${teamColumns.GAME_ID} = $gameId " +
                "AND ${teamColumns.TEAM_ID} = $teamId"
        val result = dbHelper.executeSqlQuery(sql)

        result.next()
        val teamRowId = result.getLong(ID)
        val bans = banDAO.getAllBansByTeamRowId(teamRowId)
        dbHelper.connect()  // todo this connect disconnect shit is pretty yuck and bug inducing, would love to find a fix to this
        val teamResult = result.produceTeam(bans)
        result.close()
        return teamResult
    }

    /**
     * Get all [Team] instances for a [Match]
     * @param gameId The id of the match
     */
    fun getAllTeamsForGameId(gameId : Long) : ArrayList<Team> {
        dbHelper.connect()
        val sql = "SELECT * FROM $TEAM_TABLE " +
                "WHERE ${teamColumns.GAME_ID} = $gameId"
        val result = dbHelper.executeSqlQuery(sql)

        val teams = ArrayList<Team>()
        while(result.next()) {
            val teamRowId = result.getLong(ID)
            val bans = banDAO.getAllBansByTeamRowId(teamRowId)
            dbHelper.connect()
            teams.add(result.produceTeam(bans))
        }
        result.close()
        return teams
    }
}