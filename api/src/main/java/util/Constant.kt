package util

/**
 * @author Josiah Kendall
 */
class Constant {
    val DEFAULT_HOST = "jdbc:mysql://localhost:3306/local_lolanlaytics"
    val DEFAULT_USERNAME = "root"
    val DEFAULT_PASSWORD = "Idnw2bh2"

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
         * A stat that represents the total for that user over the whole game. This is represented
         * by one graph. Kills is an example of this.
         */
        val HALF_STAT = 2

        /**
         * A stat that has an early game, mid game and late game stat. This meas that it is broken
         * down into three separate graphs. An Example is Creeps Per Minute
         */
        val FULL_STAT = 3
    }

    object GameStage {
        const val EARLY_GAME = "EarlyGame"
        const val MID_GAME = "MidGame"
        const val LATE_GAME = "LateGame"
    }

}
