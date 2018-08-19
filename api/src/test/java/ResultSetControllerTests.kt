
import extensions.ResultSetController
import org.junit.Test
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import java.sql.ResultSet

class ResultSetControllerTests {

    @Test
    fun testThatWeCanCaluculateCorrectValuesFromPerfromanceProfile() {
        val key1 = "heroKey1"
        val key1Val = 5f
        val villanKey1 = "villanKey1"
        val key2 = "heroKey2"
        val key2Val = 1f
        val villanKey2 = "villanKey2"
        val float1 = 60f
        val float2 = 50f
        val resultSet = mock(ResultSet::class.java)
        `when`(resultSet.getObject(key1)).thenReturn(60)
        `when`(resultSet.getObject(villanKey1)).thenReturn(40)
        `when`(resultSet.getObject(key2)).thenReturn(60)
        `when`(resultSet.getObject(villanKey2)).thenReturn(40)

        val performanceProfile = HashMap<String, Float>()
        performanceProfile.put(key1,key1Val )
        performanceProfile.put(villanKey1,key1Val )
        performanceProfile.put(key2,key2Val )
        performanceProfile.put(villanKey2,key2Val )
        val resultSetController = ResultSetController(resultSet)
        val headToHeadStat = resultSetController.produceHeadToHeadStat(performanceProfile)
        assert(headToHeadStat.heroStatValue == 0.6f)
        assert(headToHeadStat.enemyStatValue == 0.4f)


    }
}