package database.tables

/**
 * @author Josiah Kendall
 *
 * An object with a bunch of constants that should be used to refer to the database columns with.
 */
object StatColumns {

    const val GAME_ID = "gameId"
    const val HERO_SUMMONER_ID = "heroSummonerId"
    const val HERO_CHAMP_ID = "heroChampId"
    const val VILLAN_CHAMP_ID = "villanChampId"
    const val HERO_TEAM_ID = "heroTeamId"
    const val VILLAN_TEAM_ID = "villanTeamId"
    const val HERO_RESULT = "heroWin"
    const val VILLAN_RESULT = "villanWin" // todo fix redundancy here
    const val HERO_TEAM_TOWER_KILLS = "heroTeamTowerKills"
    const val VILLAN_TEAM_TOWER_KILLS = "villanTeamTowerKills"
    const val HERO_TEAM_DRAGON_KILLS = "heroTeamDragonKills"
    const val VILLAN_TEAM_DRAGON_KILLS = "villanTeamDragonKills"
    const val HERO_TEAM_RIFT_HERALD_KILLS = "heroTeamRiftHeraldKills"
    const val VILLAN_TEAM_RIFT_HERALD_KILLS = "villanTeamRiftHeraldKills"
    const val HERO_TEAM_BARON_KILLS = "heroTeamBaronKills"
    const val VILLAN_TEAM_BARON_KILLS = "villanTeamBaronKills"
    const val HERO_KILLS = "heroKills"
    const val VILLAN_KILLS = "villanKills"
    const val HERO_DEATHS = "heroDeaths"
    const val VILLAN_DEATHS= "villanDeaths"
    const val HERO_ASSISTS= "heroAssists"
    const val VILLAN_ASSISTS= "villanAssists"
    const val HERO_WARDS_PLACED = "heroWardsPlaced"
    const val VILLAN_WARDS_PLACED = "villanWardsPlaced"
    const val HERO_WARDS_KILLED= "heroWardsKilled"
    const val VILLAN_WARDS_KILLED= "villanWardsKilled"
    const val HERO_EARLY_GAME_CREEPS = "heroCreepsEarlyGame"
    const val HERO_MID_GAME_CREEPS = "heroCreepsMidGame"
    const val HERO_LATE_GAME_CREEPS = "heroCreepsLateGame"
    const val VILLAN_EARLY_GAME_CREEPS = "villanCreepsEarlyGame"
    const val VILLAN_MID_GAME_CREEPS = "villanCreepsMidGame"
    const val VILLAN_LATE_GAME_CREEPS = "villanCreepsLateGame"
    const val HERO_EARLY_GAME_XP = "heroXpEarlyGame"
    const val HERO_MID_GAME_XP = "heroXpMidGame"
    const val HERO_LATE_GAME_XP = "heroXpLateGame"
    const val VILLAN_EARLY_GAME_XP = "villanXpEarlyGame"
    const val VILLAN_MID_GAME_XP = "villanXpMidGame"
    const val VILLAN_LATE_GAME_XP = "villanXpLateGame"
    const val HERO_EARLY_GAME_GOLD = "heroGoldEarlyGame"
    const val HERO_MID_GAME_GOLD = "heroGoldMidGame"
    const val HERO_LATE_GAME_GOLD = "heroGoldLateGame"
    const val VILLAN_EARLY_GAME_GOLD = "villanGoldEarlyGame"
    const val VILLAN_MID_GAME_GOLD = "villanGoldMidGame"
    const val VILLAN_LATE_GAME_GOLD = "villanGoldLateGame"
    const val HERO_EARLY_GAME_DAMAGE = "heroDamageEarlyGame"
    const val HERO_MID_GAME_DAMAGE = "heroDamageMidGame"
    const val HERO_LATE_GAME_DAMAGE = "heroDamageLateGame"
    const val VILLAN_EARLY_GAME_DAMAGE = "villanDamageEarlyGame"
    const val VILLAN_MID_GAME_DAMAGE = "villanDamageMidGame"
    const val VILLAN_LATE_GAME_DAMAGE = "villanDamageLateGame"

}