package extensions

import model.champion.Champion
import model.champion.ChampionImage
import model.match.*
import model.matchlist.MatchSummary
import model.stats.GameStageDelta
import util.ColumnNames
import util.columnnames.*
import java.sql.ResultSet

/**
 * @author Josiah Kendall
 *
 * Extensions for the result set class
 */
val columnNames = ColumnNames()
val statColumns = StatsColumns()
val gameStageColumns = GameStageColumns()
val particpantColumns = ParticipantColumns()
val banColumns = BanColumns()
val teamColumns = TeamColumns()
val matchColumns = MatchColumns()

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
val ACCOUNT_ID = "accountId"
val SUMMONER_NAME = "summonerName"
val CURRENT_PLATFORM_ID = "currentPlatformId"
val MATCH_HISTORY_URI = "matchHistoryUri"
val PROFILE_ICON = "profileIcon"
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
val PARTICIPANT_ID = "ParticipantId"


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

fun ResultSet.producePlayer() : Player {
    val platformId : String = getString(PLATFORM_ID)
    val accountId : Long = getLong(ACCOUNT_ID)
    val summonerName : String = getString(SUMMONER_NAME)
    val summonerId : Long = getLong(SUMMONER_ID)
    val currentPlatformId : String = getString(CURRENT_PLATFORM_ID)
    val matchHistoryUri : String = getString(MATCH_HISTORY_URI)
    val profileIcon : Int = getInt(PROFILE_ICON)

    return Player(platformId, accountId, summonerName, summonerId, currentPlatformId, matchHistoryUri, profileIcon)
}

fun ResultSet.produceParticipantIdentity() : ParticipantIdentity {
    val player = producePlayer()
    val participantId = getInt(PARTICIPANT_ID)
    val gameId = getLong(GAME_ID)
    return ParticipantIdentity(participantId, gameId, player)
}

fun ResultSet.produceMastery(): Mastery {
    val masteryId = getInt(columnNames.MASTERY_ID)
    val rank = getInt(columnNames.RANK)
    return Mastery(masteryId, rank)
}

fun ResultSet.produceRune(): Rune {
    val runeId = getInt(columnNames.RUNE_ID)
    val rank = getInt(columnNames.RANK)
    return Rune(runeId, rank)
}

fun ResultSet.produceStats() : Stats {
    return Stats(getInt(statColumns.PARTICIPANT_ID),
            getBoolean(statColumns.WIN),
            getInt(statColumns.ITEM0),
            getInt(statColumns.ITEM1),
            getInt(statColumns.ITEM2),
            getInt(statColumns.ITEM3),
            getInt(statColumns.ITEM4),
            getInt(statColumns.ITEM5),
            getInt(statColumns.ITEM6),
            getInt(statColumns.KILLS),
            getInt(statColumns.DEATHS),
            getInt(statColumns.ASSISTS),
            getInt(statColumns.LARGEST_KILLING_SPREE),
            getInt(statColumns.LARGEST_MULTI_KILL),
            getInt(statColumns.KILLING_SPREES),
            getInt(statColumns.LONGEST_TIME_SPENT_LIVING),
            getInt(statColumns.DOUBLE_KILLS),
            getInt(statColumns.TRIPLE_KILLS),
            getInt(statColumns.QUADRA_KILLS),
            getInt(statColumns.PENTA_KILLS),
            getInt(statColumns.UNREAL_KILLS),
            getInt(statColumns.TOTAL_DAMAGE_DEALT),
            getInt(statColumns.MAGIC_DAMAGE_DEALT),
            getInt(statColumns.PHYSICAL_DAMAGE_DEALT),
            getInt(statColumns.TRUE_DAMAGE_DEALT),
            getInt(statColumns.LARGEST_CRITCAL_STRIKE),
            getInt(statColumns.TOTAL_DAMAGE_DEALT_TO_CHAMPIONS),
            getInt(statColumns.TOTAL_MAGIC_DAMAGE_DEALT_TO_CHAMPIONS),
            getInt(statColumns.PHYSICAL_DAMAGE_DEALT_TO_CHAMPIONS),
            getInt(statColumns.TRUE_DAMAGE_DEALT_TO_CHAMPIONS),
            getInt(statColumns.TOTAL_HEAL),
            getInt(statColumns.TOTAL_UNITS_HEALED),
            getInt(statColumns.DAMAGE_SELF_MITIGATED),
            getInt(statColumns.DAMAGE_DEALT_TO_OBJECTIVES),
            getInt(statColumns.DAMAGE_DEALT_TO_TURRETS),
            getInt(statColumns.VISION_SCORE),
            getInt(statColumns.TIME_CCING_OTHERS),
            getInt(statColumns.TOTAL_DAMAGE_TAKEN),
            getInt(statColumns.MAGIC_DAMAGE_TAKEN),
            getInt(statColumns.PHYSICAL_DAMAGE_TAKEN),
            getInt(statColumns.TRUE_DAMAGE_TAKEN),
            getInt(statColumns.GOLD_EARNED),
            getInt(statColumns.GOLD_SPENT),
            getInt(statColumns.TURRENT_KILLS),
            getInt(statColumns.INHIBITOR_KILLS),
            getInt(statColumns.TOTAL_MINIONS_KILLED),
            getInt(statColumns.NEUTRAL_MINIONS_KILLED),
            getInt(statColumns.NEUTRAL_MINIONS_KILLED_TEAM_JUNGLE),
            getInt(statColumns.NEUTRAL_MINIONS_KILLED_ENEMY_JUNGLE),
            getInt(statColumns.TOTAL_TIME_CROWD_CONTROL_DEALT),
            getInt(statColumns.CHAMP_LEVEL),
            getInt(statColumns.VISION_WARDS_BOUGHT_IN_GAME),
            getInt(statColumns.SIGHT_WARDS_BOUGHT_IN_GAME),
            getInt(statColumns.WARDS_PLACED),
            getInt(statColumns.WARDS_KILLED),
            getBoolean(statColumns.FIRST_BLOOD_KILLS),
            getBoolean(statColumns.FIRST_BLOOD_ASSIST),
            getBoolean(statColumns.FIRST_TOWER_KILL),
            getBoolean(statColumns.FIRST_TOWER_ASSIST),
            getBoolean(statColumns.FIRST_INHIBITOR_KILL),
            getBoolean(statColumns.FIRST_INHIBITOR_ASSIST),
            getInt(statColumns.COMBAT_PLAYER_SCORE),
            getInt(statColumns.OBJECTIVE_PLAYER_SCORE),
            getInt(statColumns.TOTAL_PLAYER_SCORE),
            getInt(statColumns.TOTAL_SCORE_RANK))
}

fun ResultSet.produceGameStageStat() : GameStageDelta {
    val unit =  GameStageDelta()
    unit.zeroToTen = getDouble(gameStageColumns.ZERO_TO_TEN)
    unit.tenToTwenty = getDouble(gameStageColumns.TEN_TO_TWENTY)
    unit.twentyToThirty = getDouble(gameStageColumns.TWENTY_TO_THIRTY)
    unit.thirtyToEnd = getDouble(gameStageColumns.THIRTY_TO_END)

    return unit
}

fun ResultSet.produceTimeline(creepsPerMin : GameStageDelta,
                              xpPerMin : GameStageDelta,
                              goldPerMin : GameStageDelta,
                              damageTakenPerMin : GameStageDelta,
                              csDiffPerMin :GameStageDelta,
                              xpDiffPerMin :GameStageDelta,
                              damageTakenDiffPerMin : GameStageDelta) : Timeline {

    val participantId = getInt(PARTICIPANT_ID)
    val role = getString(ROLE)
    val lane = getString(LANE)
    return Timeline(participantId,
            creepsPerMin,
            xpPerMin,
            goldPerMin,
            csDiffPerMin,
            xpDiffPerMin,
            damageTakenPerMin,
            damageTakenDiffPerMin,
            role,
            lane)

}

fun ResultSet.produceParticipant(timeline: Timeline,
                                 stats: Stats,
                                 masteries : ArrayList<Mastery>,
                                 runes : ArrayList<Rune>) : Participant {

    val particpantId = getInt(particpantColumns.PARTICIPANT_ID)
    val teamId = getInt(particpantColumns.TEAM_ID)
    val championId = getInt(particpantColumns.CHAMPION_ID)
    val spell1Id = getInt(particpantColumns.SPELL_1_ID)
    val spell2Id = getInt(particpantColumns.SPELL_2_ID)
    val hsat = getString(particpantColumns.HIGHEST_SEASON_ACHIEVED_TIER)

    return Participant(particpantId,
            teamId,
            championId,
            spell1Id,
            spell2Id,
            masteries, runes,
            hsat,
            stats,
            timeline)
}

fun ResultSet.produceBan() : Ban {
    return Ban(getInt(banColumns.CHAMPION_ID), getInt(banColumns.PICK_TURN))
}

fun ResultSet.produceTeam(bans : ArrayList<Ban>) : Team {
    return Team(getInt(teamColumns.TEAM_ID),
            getString(teamColumns.WIN),
            getBoolean(teamColumns.FIRST_BLOOD),
            getBoolean(teamColumns.FIRST_TOWER),
            getBoolean(teamColumns.FIRST_INHIBITOR),
            getBoolean(teamColumns.FIRST_BARON),
            getBoolean(teamColumns.FIRST_DRAGON),
            getBoolean(teamColumns.FIRST_RIFT_HERALD),
            getInt(teamColumns.TOWER_KILLS),
            getInt(teamColumns.INHIBITOR_KILLS),
            getInt(teamColumns.BARON_KILLS),
            getInt(teamColumns.DRAGON_KILLS),
            getInt(teamColumns.VILE_MAW_KILLS),
            getInt(teamColumns.RIFT_HERALD_KILLS),
            getInt(teamColumns.DOMINION_VICOTORY_SCORE),
            bans)
}

fun ResultSet.produceMatch(teams : ArrayList<Team>,
                           participants : ArrayList<Participant>,
                           participantIdentities : ArrayList<ParticipantIdentity>) : Match{
    return Match(getLong(matchColumns.GAME_ID),
            getString(matchColumns.PLATFORM_ID),
            getLong(matchColumns.GAME_CREATION),
            getLong(matchColumns.GAME_DURATION),
            getInt(matchColumns.QUEUE_ID),
            getInt(matchColumns.MAP_ID),
            getInt(matchColumns.SEASON_ID),
            getString(matchColumns.GAME_VERSION),
            getString(matchColumns.GAME_MODE),
            getString(matchColumns.GAME_TYPE),
            teams,
            participants,
            participantIdentities)
}