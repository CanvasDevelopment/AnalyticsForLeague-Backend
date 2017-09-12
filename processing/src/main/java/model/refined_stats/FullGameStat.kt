package model.refined_stats

/**
 * @author Josiah Kendall
 */
data class FullGameStat(val gameId : Long,
                        val kills : Int,
                        val deaths : Int,
                        val assists : Int,
                        val wardsPlaced : Int,
                        val wardsKilled : Int) {

    /**
     * Override of equals, so that we can check if the stats are properly the same
     */
    override fun equals(other: Any?): Boolean {
        if (other is FullGameStat) {
            return gameId == other.gameId
                    && kills == other.kills
                    && deaths == other.deaths
                    && assists == other.assists
                    && wardsPlaced == other.wardsPlaced
                    && wardsKilled == other.wardsKilled
        }
        return false
    }
}