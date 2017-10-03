package util

/**
 * @author Josiah Kendall
 */
class TableNames {

    private val role = Role()

    /**
     * Produce the string value of a role.
     * @param roleValue The Integer representation of a role.
     * @return The string value of the role. e.g 1 = "top"
     */
    private fun produceRoleString(roleValue: Int) : String {
        when(roleValue) {
            role.TOP -> return "top"
            role.JUNGLE -> return "jungle"
            role.MID -> return "mid"
            role.ADC -> return "adc"
            role.SUPPORT -> return "support"
        }
        throw IllegalStateException("Value given was not a valid role")
    }

    /**
     * Construct the table name that we want to retrieve the stats from a certain role
     * @param role  The role that we want the stats for
     * @return      The table name that we can use to pull data from the table.
     */
    fun getRefinedStatsTableName(role : Int) : String {
        return "${produceRoleString(role)}_summarystats"
    }

    /**
     * Construct the table name that we want to retrieve the stats from a certain role
     * @param role  The role that we want the stats for
     * @return      The table name that we can use to pull data from the table.
     */
    fun getRefinedStatsTableName(role : String) : String {
        return "${role}_summarystats"
    }
}