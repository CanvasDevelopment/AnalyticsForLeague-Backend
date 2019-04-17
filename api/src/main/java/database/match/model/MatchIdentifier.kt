package database.match.model

class MatchIdentifier(val matchId: Long,
                      val role : String,
                      val lane : String,
                      val champKey : Int,
                      val timestamp : Long,
                      val summonerId : String)