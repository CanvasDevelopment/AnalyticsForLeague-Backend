package db.champion

import com.github.salomonbrys.kodein.Kodein
import com.github.salomonbrys.kodein.KodeinAware
import com.github.salomonbrys.kodein.instance
import db.requests.DBHelper
import di.KodeinManager
import extensions.*
import model.champion.Champion
import model.champion.ChampionImage

/**
 * @author Josiah Kendall
 */
class ChampionDAO(val dbHelper : DBHelper) {

    private val CHAMPION_TABLE = "Champion"
    private val CHAMPION_IMAGE_TABLE = "ChampionImage"
    private val CHAMP_ID_COLUMN = "Id"
    private val FULL_COLUMN = "Full"
    private val SPRITE_COLUMN = "Sprite"
    private val IMAGE_GROUP_COLUMN = "ImageGroup"
    private val X_COLUMN = "X"
    private val Y_COLUMN = "Y"
    private val W_COLUMN = "W"
    private val H_COLUMN = "H"

    fun saveChampion(champion : Champion) : Long {
        dbHelper.connect()
        val championImageId = saveChampionImage(champion.image)
        val insertString = "INSERT into $CHAMPION_TABLE values (" +
                "${champion.id}, " +
                "'${champion.key}', " +
                "'${champion.name}', " +
                "'${champion.title}', " +
                "$championImageId) On DUPLICATE KEY UPDATE " +
                "$ID = ${champion.id}," +
                "$CHAMP_KEY_COLUMN = '${champion.key}'," +
                "$CHAMP_NAME_COLUMN = '${champion.name}'," +
                "$CHAMP_TITLE_COLUMN = '${champion.title}'"
        val result = dbHelper.executeSQLScript(insertString)
        // log this
        return result
    }

    internal fun saveChampionImage(champImage : ChampionImage) : Long {

        val insertString = "INSERT INTO $CHAMPION_IMAGE_TABLE (" +
                "$FULL_COLUMN, " +
                "$SPRITE_COLUMN, " +
                "$IMAGE_GROUP_COLUMN, " +
                "$X_COLUMN, " +
                "$Y_COLUMN, " +
                "$W_COLUMN, " +
                "$H_COLUMN) VALUES (" +
                "'${champImage.full}', " +
                "'${champImage.sprite}'," +
                "'${champImage.group}'," +
                "${champImage.x}," +
                "${champImage.y}," +
                "${champImage.w}," +
                "${champImage.h}) ON DUPLICATE KEY UPDATE " +
                "$FULL_COLUMN = '${champImage.full}'," +
                "$SPRITE_COLUMN = '${champImage.sprite}'," +
                "$IMAGE_GROUP_COLUMN = '${champImage.group}'," +
                "$X_COLUMN = ${champImage.x}," +
                "$Y_COLUMN = ${champImage.y}," +
                "$H_COLUMN = ${champImage.h}," +
                "$W_COLUMN = ${champImage.w}"
        return dbHelper.executeSQLScript(insertString)
    }

    fun getChampion(champId : Long) : Champion? {
        val queryString = "Select * from $CHAMPION_TABLE WHERE $CHAMP_ID_COLUMN = $champId"
        dbHelper.connect()
        val result = dbHelper.executeSqlQuery(queryString)

        if (result.next()) {
            val champImageId = result.getLong(CHAMP_IMAGE_ID_COLUMN)
            val championImage = getChampionImage(champImageId)
            val champion = result.produceChampion(championImage)
            result.close()
            return champion
        }
        result.close()
        return null
    }

    internal fun getChampionImage(champImageId: Long) : ChampionImage {
        val queryString = "SELECT * from $CHAMPION_IMAGE_TABLE WHERE $CHAMP_ID_COLUMN = $champImageId"
        val result = dbHelper.executeSqlQuery(queryString)
        result.next()
        val champImage = result.produceChampionImage()
        result.close()
        return champImage
    }
}