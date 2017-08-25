package model.match

/**
 * @author Josiah Kendall
 */

data class Player(val platformId : String,
                  val accountId : Long,
                  val summonerName : String,
                  val summonerId : Long,
                  val currentPlatformId : String,
                  val matchHistoryUri : String,
                  val profileIcon : Int)
