package db.match

import db.DBHelper
import extensions.produceStats
import model.match.Stats
import util.columnnames.StatsColumns

/**
 * @author Josiah Kendall
 */
class StatDAO(val dbHelper: DBHelper) {
    val statsColumns = StatsColumns()
    val STATS_TABLE = "stats"
    fun saveStats(stats: Stats, participantRowId: Long) : Long {
        val sql = "insert into $STATS_TABLE(${statsColumns.PARTICIPANT_ROW_ID},\n" +
                "${statsColumns.PARTICIPANT_ID},\n" +
                "${statsColumns.WIN},\n" +
                "${statsColumns.ITEM0},\n" +
                "${statsColumns.ITEM1},\n" +
                "${statsColumns.ITEM2},\n" +
                "${statsColumns.ITEM3},\n" +
                "${statsColumns.ITEM4},\n" +
                "${statsColumns.ITEM5},\n" +
                "${statsColumns.ITEM6},\n" +
                "${statsColumns.KILLS},\n" +
                "${statsColumns.DEATHS},\n" +
                "${statsColumns.ASSISTS},\n" +
                "${statsColumns.LARGEST_KILLING_SPREE},\n" +
                "${statsColumns.LARGEST_MULTI_KILL},\n" +
                "${statsColumns.KILLING_SPREES},\n" +
                "${statsColumns.LONGEST_TIME_SPENT_LIVING},\n" +
                "${statsColumns.DOUBLE_KILLS},\n" +
                "${statsColumns.TRIPLE_KILLS},\n" +
                "${statsColumns.QUADRA_KILLS},\n" +
                "${statsColumns.PENTA_KILLS},\n" +
                "${statsColumns.UNREAL_KILLS},\n" +
                "${statsColumns.TOTAL_DAMAGE_DEALT},\n" +
                "${statsColumns.MAGIC_DAMAGE_DEALT},\n" +
                "${statsColumns.PHYSICAL_DAMAGE_DEALT},\n" +
                "${statsColumns.TRUE_DAMAGE_DEALT}, \n" +
                "${statsColumns.LARGEST_CRITCAL_STRIKE}, \n" +
                "${statsColumns.TOTAL_DAMAGE_DEALT_TO_CHAMPIONS}, \n" +
                "${statsColumns.TOTAL_MAGIC_DAMAGE_DEALT_TO_CHAMPIONS}, \n" +
                "${statsColumns.PHYSICAL_DAMAGE_DEALT_TO_CHAMPIONS}, \n" +
                "${statsColumns.TRUE_DAMAGE_DEALT_TO_CHAMPIONS}, \n" +
                "${statsColumns.TOTAL_HEAL}, \n" +
                "${statsColumns.TOTAL_UNITS_HEALED}, \n" +
                "${statsColumns.DAMAGE_SELF_MITIGATED}, \n" +
                "${statsColumns.DAMAGE_DEALT_TO_OBJECTIVES}, \n" +
                "${statsColumns.DAMAGE_DEALT_TO_TURRETS}, \n" +
                "${statsColumns.VISION_SCORE}, \n" +
                "${statsColumns.TIME_CCING_OTHERS}, \n" +
                "${statsColumns.TOTAL_DAMAGE_TAKEN}, \n" +
                "${statsColumns.MAGIC_DAMAGE_TAKEN}, \n" +
                "${statsColumns.PHYSICAL_DAMAGE_TAKEN}, \n" +
                "${statsColumns.TRUE_DAMAGE_TAKEN}, \n" +
                "${statsColumns.GOLD_EARNED}, \n" +
                "${statsColumns.GOLD_SPENT}, \n" +
                "${statsColumns.TURRENT_KILLS}, \n" +
                "${statsColumns.INHIBITOR_KILLS}, \n" +
                "${statsColumns.TOTAL_MINIONS_KILLED}, \n" +
                "${statsColumns.NEUTRAL_MINIONS_KILLED}, \n" +
                "${statsColumns.NEUTRAL_MINIONS_KILLED_TEAM_JUNGLE}, \n" +
                "${statsColumns.NEUTRAL_MINIONS_KILLED_ENEMY_JUNGLE}, \n" +
                "${statsColumns.TOTAL_TIME_CROWD_CONTROL_DEALT}, \n" +
                "${statsColumns.CHAMP_LEVEL}, \n" +
                "${statsColumns.VISION_WARDS_BOUGHT_IN_GAME}, \n" +
                "${statsColumns.SIGHT_WARDS_BOUGHT_IN_GAME}, \n" +
                "${statsColumns.WARDS_PLACED}, \n" +
                "${statsColumns.WARDS_KILLED}, \n" +
                "${statsColumns.FIRST_BLOOD_KILLS}, \n" +
                "${statsColumns.FIRST_BLOOD_ASSIST}, \n" +
                "${statsColumns.FIRST_TOWER_KILL}, \n" +
                "${statsColumns.FIRST_TOWER_ASSIST}, \n" +
                "${statsColumns.FIRST_INHIBITOR_KILL}, \n" +
                "${statsColumns.FIRST_INHIBITOR_ASSIST}, \n" +
                "${statsColumns.COMBAT_PLAYER_SCORE}, \n" +
                "${statsColumns.OBJECTIVE_PLAYER_SCORE}, \n" +
                "${statsColumns.TOTAL_PLAYER_SCORE}, \n" +
                "${statsColumns.TOTAL_SCORE_RANK}) values (\n" +
                 "$participantRowId,\n" +
                "${stats.participantId},\n" +
                "${stats.win},\n" +
                "${stats.item0},\n" +
                "${stats.item1},\n" +
                "${stats.item2},\n" +
                "${stats.item3},\n" +
                "${stats.item4},\n" +
                "${stats.item5},\n" +
                "${stats.item6},\n" +
                "${stats.kills},\n" +
                "${stats.deaths},\n" +
                "${stats.assists},\n" +
                "${stats.largestKillingSpree},\n" +
                "${stats.largestMultiKill},\n" +
                "${stats.killingSprees},\n" +
                "${stats.longestTimeSpentLiving},\n" +
                "${stats.doubleKills},\n" +
                "${stats.tripleKills},\n" +
                "${stats.quadraKills},\n" +
                "${stats.pentaKills},\n" +
                "${stats.unrealKills},\n" +
                "${stats.totalDamageDealt},\n" +
                "${stats.magicDamageDealt},\n" +
                "${stats.physicalDamageDealt},\n" +
                "${stats.trueDamageDealt}, \n" +
                "${stats.largestCriticalStrike}, \n" +
                "${stats.totalDamageDealtToChampions}, \n" +
                "${stats.magicDamageDealtToChampions}, \n" +
                "${stats.physicalDamageDealtToChampions}, \n" +
                "${stats.trueDamageDealtToChampions}, \n" +
                "${stats.totalHeal}, \n" +
                "${stats.totalUnitsHealed}, \n" +
                "${stats.damageSelfMitigated}, \n" +
                "${stats.damageDealtToObjectives}, \n" +
                "${stats.damageDealtToTurrets}, \n" +
                "${stats.visionScore}, \n" +
                "${stats.timeCCingOthers}, \n" +
                "${stats.totalDamageTaken}, \n" +
                "${stats.magicDamageTaken}, \n" +
                "${stats.physicalDamageTaken}, \n" +
                "${stats.trueDamageTaken}, \n" +
                "${stats.goldEarned}, \n" +
                "${stats.goldSpent}, \n" +
                "${stats.turretKills}, \n" +
                "${stats.inhibitorKills}, \n" +
                "${stats.totalMinionsKilled}, \n" +
                "${stats.neutralMinionsKilled}, \n" +
                "${stats.neutralMinionsKilledTeamJungle}, \n" +
                "${stats.neutralMinionsKilledEnemyJungle}, \n" +
                "${stats.totalTimeCrowdControlDealt}, \n" +
                "${stats.champLevel}, \n" +
                "${stats.visionWardsBoughtInGame}, \n" +
                "${stats.sightWardsBoughtInGame}, \n" +
                "${stats.wardsPlaced}, \n" +
                "${stats.wardsKilled}, \n" +
                "${stats.firstBloodKill}, \n" +
                "${stats.firstBloodAssist}, \n" +
                "${stats.firstTowerKill}, \n" +
                "${stats.firstTowerAssist}, \n" +
                "${stats.firstInhibitorKill}, \n" +
                "${stats.firstInhibitorAssist},\n" +
                "${stats.combatPlayerScore}, \n" +
                "${stats.objectivePlayerScore}, \n" +
                "${stats.totalPlayerScore}, \n" +
                "${stats.totalScoreRank})"
        return dbHelper.executeSQLScript(sql)
    }

    fun getStatForParticipant(particpantRowId: Long) : Stats {
        val sql = "SELECT * from $STATS_TABLE " +
                "WHERE ${statsColumns.PARTICIPANT_ROW_ID} = $particpantRowId"
        val result = dbHelper.executeSqlQuery(sql)
        result.next()
        return result.produceStats()
    }


}