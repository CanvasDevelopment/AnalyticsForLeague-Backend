package util

/**
 * @author Josiah Kendall
 *
 * Just easier access for our roles and lanes so that we dont have to keep typing them
 */
val TOP = "Top"
val MID = "Mid"
val BOT = "Bottom"
val JUNGLE = "Jungle"
val SUPPORT = "Support"

val SOLO = "SOLO"
val DUO = "DUO"
val DUO_CARRY = "DUO_CARRY"
val DUO_SUPPORT = "DUO_SUPPORT"
val NONE = "None"
val ADC = "ADC"

class RolesAndLanes {
    fun getRole(role : Int) : String {
        when(role) {
            1 -> return "SOLO"
            2 -> return "NONE"
            3 -> return "SOLO"
            4 -> return "DUO_CARRY"
            5 -> return "DUO_SUPPORT"
        }

        throw IllegalStateException("WHAT THE FUCK ARE YOU DOING DON'T DO THIS SHIT")
    }

    fun getLane(role : Int) : String {
        when (role) {
            1 -> return "TOP"
            2 -> return "JUNGLE"
            3 -> return "MID"
            4 -> return "BOTTOM"
            5 -> return "BOTTOM"
        }

        throw IllegalStateException("WHAT THE FUCK ARE YOU DOING DON'T DO THIS SHIT")
    }
}


