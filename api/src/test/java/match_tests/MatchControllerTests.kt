package match_tests

import api.match.MatchController
import database.match.MatchDao
import org.junit.Test
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import util.TableNames
import java.sql.ResultSet

class MatchControllerTests {

    @Test
    fun makeSureThatWeCanTestResultSet() {
        val matchController = MatchController(mock(MatchDao::class.java),mock(TableNames::class.java))
        val resultSet : ResultSet = mock(ResultSet::class.java)
        `when`(resultSet.first()).thenReturn(true)
        assert(matchController.testResultSet(resultSet))

    }
}