package model.match

/**
 * @author Josiah Kendall
 */

data class Player(var platformId : String,
                  var accountId : Long,
                  var summonerName : String,
                  var summonerId : Long,
                  var currentPlatformId : String,
                  var matchHistoryUri : String,
                  var profileIcon : Int)
