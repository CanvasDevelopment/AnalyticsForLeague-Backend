package model.match

import com.google.gson.annotations.SerializedName

/**
 * @author Josiah Kendall
 */
data class Match(@SerializedName("gameId") val gameId : Long,
                 @SerializedName("platformId") val platformId : String,
                 @SerializedName("gameCreation") val gameCreation : Long,
                 @SerializedName("gameDuration") val gameDuration : Long,
                 @SerializedName("queueId") val queueId : Int,
                 @SerializedName("mapId") val mapId : Int,
                 @SerializedName("seasonId") val seasonId : Int,
                 @SerializedName("gameVersion") val gameVersion: String,
                 @SerializedName("gameMode") val gameMode : String,
                 @SerializedName("gameType") val gameType : String,
                 @SerializedName("teams") val teams : ArrayList<Team>,
                 @SerializedName("participants") val participants : ArrayList<Participant>,
                 @SerializedName("participantIdentities") val participantIdentities: ArrayList<ParticipantIdentity>)
