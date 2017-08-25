package db_test.champion

import com.github.salomonbrys.kodein.instance
import db.DBHelper
import db.champion.ChampionDAO
import di.KodeinManager
import junit.framework.Assert.assertTrue
import model.champion.Champion
import model.champion.ChampionImage
import org.junit.Test

/**
 * @author Josiah Kendall
 */

class ChampionTests {

    @Test
    fun ensure_WeCanSaveAndLoadAChampionWithChampionImageOnIt() {
        val kodeinManager = KodeinManager()

        val championImage = ChampionImage("test", "test", "test", 1, 1, 1, 1)
        val champion = Champion(23,"jax","jax", "Grandmaster", championImage)

        val championDAO = ChampionDAO(kodeinManager.kodein)
        championDAO.saveChampion(champion)

        val c2 = championDAO.getChampion(23)

        assertTrue(c2?.title == champion.title)
        assertTrue(c2?.name == champion.name)
        assertTrue(c2?.key == champion.key)
        assertTrue(c2?.image?.full == champion.image.full)
        assertTrue(c2?.image?.group == champion.image.group)
        assertTrue(c2?.image?.sprite == champion.image.sprite)

    }
}
