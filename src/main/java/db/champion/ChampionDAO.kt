package db.champion

import db.DBHelper
import extensions.*
import model.champion.Champion
import model.champion.ChampionImage

/**
 * @author Josiah Kendall
 */
class ChampionDAO(val dbHelper: DBHelper) {

    val CHAMPION_TABLE = "champion"
    val CHAMPION_IMAGE_TABLE = "championimage"
    val CHAMP_ID_COLUMN = "Id"
    val FULL_COLUMN = "Full"
    val SPRITE_COLUMN = "Sprite"
    val IMAGE_GROUP_COLUMN = "ImageGroup"
    val X_COLUMN = "X"
    val Y_COLUMN = "Y"
    val W_COLUMN = "W"
    val H_COLUMN = "H"

    fun saveChampion(champion : Champion) : Int {
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
        return dbHelper.ExecuteSqlScript(insertString)
    }

    internal fun saveChampionImage(champImage : ChampionImage) : Int {
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

        return dbHelper.ExecuteSqlScript(insertString)
    }

    fun getChampion(champId : Int) : Champion? {
        val queryString = "Select * from $CHAMPION_TABLE WHERE $CHAMP_ID_COLUMN = $champId"
        val result = dbHelper.ExecuteSqlQuery(queryString)

        if (result.next()) {
            val champImageId = result.getInt(CHAMP_IMAGE_ID_COLUMN)
            val championImage = getChampionImage(champImageId)
            return result.produceChampion(championImage)
        }
        return null
    }

    internal fun getChampionImage(champImageId: Int) : ChampionImage {
        val queryString = "SELECT * from $CHAMPION_IMAGE_TABLE WHERE $CHAMP_ID_COLUMN = $champImageId"
        val result = dbHelper.ExecuteSqlQuery(queryString)
        result.next()
        return result.produceChampionImage()
    }
}