package model.match

/**
 * @author Josiah Kendall
 */
data class Team(val teamId : Int,
                val win : String,
                val firstBlood : Boolean,
                val firstTower : Boolean,
                val firstInhibitor : Boolean,
                val firstBaron : Boolean,
                val firstDragon : Boolean,
                val firstRiftHerald : Boolean,
                val towerKills : Int,
                val inhibitorKills : Int,
                val baronKills : Int,
                val dragonKills : Int,
                val vileMawKills : Int,
                val riftHeraldKills : Int,
                val dominionVictoryScore : Int,
                val bans : ArrayList<Bans>)