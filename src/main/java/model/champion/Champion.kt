package model.champion

/**
 * @author Josiah Kendall
 */
data class Champion(
        val id : Int,
        val key : String,
        val name : String,
        val title : String,
        val image : ChampionImage)
