package db.champion

import db.requests.DBHelper
import util.columnnames.ChampDataColumns
import java.sql.ResultSet

class ChampDataDAO(private val dbHelper: DBHelper) {
    val champName = "champ_name"
    val champCount = "champCount"
    val champDataColumns = ChampDataColumns()
    val tableName = "ChampData"
    val version : String = "version"
    val id : String = "id"
    val key : String = "champ_key"
    val name : String = "champ_name"
    val title : String = "title"
    val blurb : String = "blurb"

    fun saveChampData(champData : ChampData) : Long {

        dbHelper.connect()
        if (exists(champData.id)) {
            delete(champData.id)
        }
        // check if there is going to be a conflict

        val insertString = "insert into $tableName(\n" +
            "                               version,\n" +
            "                               id,\n" +
            "                               champ_key,\n" +
            "                               champ_name,\n" +
            "                               title,\n" +
                "                           burb) values (\n" +
                "                                          '${champData.version}',\n" +
                "                                          '${champData.id.replace("'","''")}',\n" +
                "                                          '${champData.key}',\n" +
                "                                          '${champData.name.replace("'", "''")}',\n" +
                "                                          'todo title',\n" +
                "                                          'todo'\n" +
                "                                                         )"
        val result = dbHelper.executeSQLScript(insertString)

        return result
    }

    fun delete(id : String) {
        val sql = "delete from $tableName " +
                "where id = '$id'"
        dbHelper.executeSQLScript(sql)
    }

    fun exists(id : String) : Boolean {
        val sql = "select * from $tableName " +
                "where id = '$id'"
        val result = dbHelper.executeSqlQuery(sql)
        return result.first()
    }

    fun loadChampData(champId : String, summonerId: Long) : ChampData {
        dbHelper.connect()
        val sql = "SELECT * from $tableName " +
                "WHERE Id = '$champId'"
        val result = dbHelper.executeSqlQuery(sql)
        result.next()
        return result.produceChampData(summonerId)
    }

    /**
     * Load a list of all the champs in league of legends.
     * Each [ChampData] in the list represents a champ.
     */
    fun loadChampDataForAllChamps(summonerId: Long) : ArrayList<ChampData> {
        dbHelper.connect()
        val champs = ArrayList<ChampData>()
        val sql = "select ChampData.champ_key,\n" +
                "      Champion,\n" +
                "       count(Champion) as champCount,\n" +
                "      champ_name,\n" +
                "       version,\n" +
                "       burb\n" +
                "from MatchSummary\n" +
                "                join ChampData\n" +
                "on champ_key = Champion\n" +
                "  and SummonerId = $summonerId\n" +
                "group by champ_key,\n" +
                "         champ_name,\n" +
                "         burb,\n" +
                "         version\n" +
                "order by champCount desc"
        val result = dbHelper.executeSqlQuery(sql)
        while(result.next()) {
            champs.add(result.produceChampData(summonerId))
        }
        return champs
    }

    fun ResultSet.produceChampData(summonerId : Long) : ChampData {
        return ChampData(
                getString(champDataColumns.version),
                getString("champ_name"),
                getString(champDataColumns.key),
                getString(champDataColumns.name),
                getString("champ_name"),
                getString(champDataColumns.blurb),
                summonerId,
                getInt(champCount))
    }
}

