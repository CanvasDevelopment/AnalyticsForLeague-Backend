import org.junit.Test
import util.Utils
import java.sql.ResultSet

class ExtensionTests {

    @Test
    fun TestThatWeCanCreateVillanKeyFromHeroKey() {
        val utils = Utils()
        val key = "Key"
        val heroKey = "hero$key"
        val villain = "villan"
        val villanKey = utils.getVillainKey(heroKey)
        assert(villanKey == villain+key)
    }
}