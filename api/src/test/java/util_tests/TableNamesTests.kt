package util_tests

import org.junit.Test
import util.TableNames

/**
 * @author Josiah Kendall
 */
class TableNamesTests {

    @Test
    fun `Make sure that we can get the correct table name`() {
        val role = "adc"
        val tableNames = TableNames()
        val tableName = tableNames.getRefinedStatsTableName(role)
        assert(tableName == "adc_SummaryStats")
    }

    @Test
    fun `Make sure that we can get the correct table name using integer value`() {
        val role = 1 // 1 = top
        val tableNames = TableNames()
        val tableName = tableNames.getRefinedStatsTableName(role)
        assert(tableName == "top_SummaryStats")
    }
}