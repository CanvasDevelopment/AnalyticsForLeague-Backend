package extensions

import model.champion.Champion
import model.champion.ChampionImage
import model.matchlist.MatchSummary
import java.sql.ResultSet

/**
 * @author Josiah Kendall
 *
 * Extensions for the result set class
 */

val MATCH_SUMMARY = "matchsummary2"
val PLATFORM_ID = "PlatformId"
val GAME_ID = "GameId"
val CHAMPION = "Champion"
val QUEUE = "Queue"
val SEASON = "Season"
val TIMESTAMP = "Timestamp"
val ROLE = "Role"
val LANE = "Lane"
val ID = "Id"
val SUMMONER_ID = "SummonerId"

// Champ column references
val CHAMP_KEY_COLUMN = "ChampKey"
val CHAMP_TITLE_COLUMN = "ChampTitle"
val CHAMP_IMAGE_ID_COLUMN = "ChampImageId"
val CHAMP_NAME_COLUMN = "ChampName"

// Champion Image Columns
val FULL_COLUMN = "Full"
val SPRITE_COLUMN = "Sprite"
val IMAGE_GROUP_COLUMN = "ImageGroup"
val X_COLUMN = "X"
val Y_COLUMN = "Y"
val W_COLUMN = "W"
val H_COLUMN = "H"

fun ResultSet.produceMatchSummary() : MatchSummary {
    val result = this
    val ms = MatchSummary()
    ms.id = result.getInt(ID)
    ms.platformId = result.getString(PLATFORM_ID)
    ms.gameId = result.getLong(GAME_ID)
    ms.champion = result.getInt(CHAMPION)
    ms.queue = result.getInt(QUEUE)
    ms.season = result.getInt(SEASON)
    ms.timestamp = result.getLong(TIMESTAMP)
    ms.role = result.getString(ROLE)
    ms.lane = result.getString(LANE)
    ms.summonerId = result.getLong(SUMMONER_ID)
    return ms
}

fun ResultSet.produceChampion(image : ChampionImage) : Champion {
    val id = getInt(ID)
    val key = getString(CHAMP_KEY_COLUMN)
    val name = getString(CHAMP_NAME_COLUMN)
    val title = getString(CHAMP_TITLE_COLUMN)

    return Champion(id, key, name, title, image)
}

fun ResultSet.produceChampionImage() : ChampionImage {

    val full = getString(FULL_COLUMN)
    val sprite = getString(SPRITE_COLUMN)
    val imageGroup = getString(IMAGE_GROUP_COLUMN)
    val x = getInt(X_COLUMN)
    val y = getInt(Y_COLUMN)
    val w = getInt(W_COLUMN)
    val h = getInt(H_COLUMN)

    return ChampionImage(full, sprite, imageGroup, x, y, w, h)
}