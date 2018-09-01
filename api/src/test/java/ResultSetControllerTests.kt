
import extensions.ResultSetController
import org.junit.Assert
import org.junit.Test
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import java.sql.ResultSet
import kotlin.test.assertEquals

class ResultSetControllerTests {

    @Test
    fun testThatWeCanCaluculateCorrectValuesFromPerformanceProfile() {
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

    @Test
    fun `Test that we can handle 0 values safely`() {
        val key1 = "heroKey1"
        val key1Val = 5f
        val villanKey1 = "villanKey1"
        val key2 = "heroKey2"
        val key2Val = 1f
        val villanKey2 = "villanKey2"
        val zeroKey = "heroKey3"
        val villanZeroKey = "villanKey3"

        val resultSet = mock(ResultSet::class.java)
        `when`(resultSet.getObject(key1)).thenReturn(60)
        `when`(resultSet.getObject(villanKey1)).thenReturn(40)
        `when`(resultSet.getObject(key2)).thenReturn(60)
        `when`(resultSet.getObject(villanKey2)).thenReturn(40)
        `when`(resultSet.getObject(zeroKey)).thenReturn(0)
        `when`(resultSet.getObject(villanZeroKey)).thenReturn(0)

        val performanceProfile = HashMap<String, Float>()
        performanceProfile.put(key1,key1Val )
        performanceProfile.put(villanKey1,key1Val )
        performanceProfile.put(key2,key2Val )
        performanceProfile.put(villanKey2,key2Val )
        performanceProfile.put(zeroKey,1f )
        performanceProfile.put(villanZeroKey,1f )
        val resultSetController = ResultSetController(resultSet)
        val headToHeadStat = resultSetController.produceHeadToHeadStat(performanceProfile)
        Assert.assertEquals(headToHeadStat.heroStatValue, 0.56666666f, 0.05f)
        Assert.assertEquals(headToHeadStat.enemyStatValue, 0.43333333f, 0.05f)
    }

    @Test
    fun `Test that we can handle 0 value for only villan`() {
        val key1 = "heroKey1"
        val key1Val = 5f
        val villanKey1 = "villanKey1"
        val key2 = "heroKey2"
        val key2Val = 1f
        val villanKey2 = "villanKey2"
        val zeroKey = "heroKey3"
        val villanZeroKey = "villanKey3"

        val resultSet = mock(ResultSet::class.java)
        `when`(resultSet.getObject(key1)).thenReturn(60)
        `when`(resultSet.getObject(villanKey1)).thenReturn(40)
        `when`(resultSet.getObject(key2)).thenReturn(60)
        `when`(resultSet.getObject(villanKey2)).thenReturn(40)
        `when`(resultSet.getObject(zeroKey)).thenReturn(10)
        `when`(resultSet.getObject(villanZeroKey)).thenReturn(0)

        val performanceProfile = HashMap<String, Float>()
        performanceProfile.put(key1,key1Val)
        performanceProfile.put(villanKey1,key1Val)
        performanceProfile.put(key2,key2Val)
        performanceProfile.put(villanKey2,key2Val)
        performanceProfile.put(zeroKey,1f)
        performanceProfile.put(villanZeroKey,1f)
        val resultSetController = ResultSetController(resultSet)
        val headToHeadStat = resultSetController.produceHeadToHeadStat(performanceProfile)
        Assert.assertEquals(headToHeadStat.heroStatValue, 0.73f,0.05f)
        Assert.assertEquals(headToHeadStat.enemyStatValue, 0.27f, 0.05f)
    }

    @Test
    fun `Test that we can handle 0 value for only hero`() {
        val key1 = "heroKey1"
        val key1Val = 5f
        val villanKey1 = "villanKey1"
        val key2 = "heroKey2"
        val key2Val = 1f
        val villanKey2 = "villanKey2"
        val zeroKey = "heroKey3"
        val villanZeroKey = "villanKey3"

        // the problem here is that one stat heavily changes things if it is zero - maybe do it
        // by percentage of the total that the difference is?
        val resultSet = mock(ResultSet::class.java)
        `when`(resultSet.getObject(key1)).thenReturn(40)
        `when`(resultSet.getObject(villanKey1)).thenReturn(60)
        `when`(resultSet.getObject(key2)).thenReturn(40)
        `when`(resultSet.getObject(villanKey2)).thenReturn(60)
        `when`(resultSet.getObject(zeroKey)).thenReturn(0)
        `when`(resultSet.getObject(villanZeroKey)).thenReturn(10)

        val performanceProfile = HashMap<String, Float>()
        performanceProfile.put(key1,key1Val)
        performanceProfile.put(villanKey1,key1Val)
        performanceProfile.put(key2,key2Val)
        performanceProfile.put(villanKey2,key2Val)
        performanceProfile.put(zeroKey,1f)
        performanceProfile.put(villanZeroKey,1f)
        val resultSetController = ResultSetController(resultSet)
        val headToHeadStat = resultSetController.produceHeadToHeadStat(performanceProfile)
        Assert.assertEquals(headToHeadStat.heroStatValue, 0.27f,0.05f)
        Assert.assertEquals(headToHeadStat.enemyStatValue, 0.73f, 0.05f)
    }
}