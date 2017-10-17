package model.refined_stats

/**
 * @author Josiah Kendall
 *
 * Data class to hold the summary stats of a single row in the refined match stats table. Each instance
 * if this class represents once row in that table. It does not represent the entire row, but rather the following columns:
 *
 *  @param summonerId        The summoner for whom this is a summary for
 *  @param champId           The champ id they used in this match
 *  @param gameId            The id of the game.
 *  @param teamId            The team they were on. Will either be 100 or 200
 */
data class TeamSummaryStat(val summonerId : Long,
                           val champId : Int,
                           val gameId : Long,
                           val teamId : Int,
                           val win : Boolean,
                           val teamTowerKills : Int,
                           val teamDragonKills : Int,
                           val teamRiftHeraldKills : Int,
                           val teamBaronKills : Int)