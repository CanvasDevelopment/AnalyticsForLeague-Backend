package util

/**
 * @author Josiah Kendall
 */
class Constant {
    object Database {
        val DEFAULT_HOST = "jdbc:mysql://127.0.0.1:3306/lol_analytics?useLegacyDatetimeCode=false&serverTimezone=UTC"
        val DEFAULT_USERNAME = "root"
        val DEFAULT_PASSWORD = "Idnw2bh2"
    }


    /**
     * Our different cardUrl types
     */
    object AnalysisCardType {

        /**
         * An add that covers half the screen.
         */
        val HALF_AD = 0

        /**
         * An add that covers the full width of the screen
         */
        val FULL_AD = 1

        /**
         * A select that represents the total for that user over the whole game. This is represented
         * by one graph. Kills is an example of this.
         */
        val HALF_STAT = 2

        /**
         * A select that has an early game, mid game and late game select. This meas that it is broken
         * down into three separate graphs. An Example is Creeps Per Minute
         */
        val FULL_STAT = 3
    }

    object GameStage {
        const val EARLY_GAME = "EarlyGame"
        const val MID_GAME = "MidGame"
        const val LATE_GAME = "LateGame"
    }

    object StatTypes {
        const val CREEPS = "Creeps"
        const val DAMAGE_DEALT = "Damage"
        const val GOLD = "Gold"
        const val XP = "Xp"
        const val WARDS_PLACED = "WardsPlaced"
        const val WARDS_KILLED = "WardsKilled"
        const val TEAM_BARON_KILLS = "TeamBaronKills"
        const val TEAM_DRAGON_KILLS = "TeamDragonKills"
        const val RIFT_HERALD_KILLS = "TeamRiftHeraldKills"
    }

    object StatAccumulators {
        const val AVG = "avg"
        const val MAX = "max"
        const val MIN = "min"
        const val SUM = "sum"
    }

    object LANE {
        const val TOP = "top"
        const val MID = "mid"
        const val JUNGLE = "jungle"
        const val ADC = "adc"
        const val SUPPORT = "support"
    }

}
