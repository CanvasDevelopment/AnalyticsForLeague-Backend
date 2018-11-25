package db_test.champion

import db.champion.ChampData
import db.champion.ChampDataDAO
import db.requests.DBHelper
import org.junit.Before
import org.junit.Test
import java.util.*
import kotlin.test.assertTrue

class ChampDataTests {

    val dbHelper = DBHelper()
    val champDataDAO = ChampDataDAO(dbHelper)

    @Before
    fun before() {
        dbHelper.connect()
//        dbHelper.executeSQLScript("delete from ${champDataDAO.tableName}")
    }

    @Test
    fun testThatWeCanSaveAndLoadChampData() {
        val champData = ChampData()
        champData.blurb ="blurb"
        champData.title ="title"
        champData.name = "name"
        champData.key = "key"
        champData.id = Random().nextInt().toString()
        champData.version = "version"

        champDataDAO.saveChampData(champData)

        val summonerId = 1L

        val champDataReturned = champDataDAO.loadChampData(champData.id, summonerId)
        assertTrue(champDataReturned.name == champData.name)
        assertTrue(champDataReturned.key == champData.key)
        assertTrue(champDataReturned.version == champData.version)
        assertTrue(champDataReturned.id == champData.id)
    }

    @Test
    fun testThatWeCanCorrectlyCheckIfAChampExists() {
        val champData = ChampData()
        champData.blurb ="blurb"
        champData.title ="title"
        champData.name = "name"
        champData.key = "key"
        champData.id = Random().nextInt().toString()
        champData.version = "version"

        val summonerId = 1L

        champDataDAO.saveChampData(champData)

        val champDataReturned = champDataDAO.loadChampData(champData.id, summonerId)
        assertTrue(champDataReturned.name == champData.name)
        assertTrue(champDataReturned.key == champData.key)
        assertTrue(champDataReturned.version == champData.version)
        assertTrue(champDataReturned.id == champData.id)

        champDataDAO.exists(champData.id)
    }

    @Test
    fun testThatWeCanLoadAllChamps() {

        val summonerId = 1L

        val before = champDataDAO.loadChampDataForAllChamps(summonerId)
        assertTrue { before.isEmpty() }
        for(index in 1..140) {
            champDataDAO.saveChampData(produceRandomChampData())
        }

        val after = champDataDAO.loadChampDataForAllChamps(summonerId)
        assertTrue { !after.isEmpty() }
        assertTrue { after.size == 140 }


    }

    @Test
    fun testThatWeCanCorrectlyDeleteAnEntry() {
        val champData = ChampData()
        champData.blurb ="blurb"
        champData.title ="title"
        champData.name = "name"
        champData.key = "key"
        champData.id = Random().nextInt().toString()
        champData.version = "version"

        champDataDAO.saveChampData(champData)

        val summonerId = 1L

        val champDataReturned = champDataDAO.loadChampData(champData.id, summonerId)
        assertTrue(champDataReturned.name == champData.name)
        assertTrue(champDataReturned.key == champData.key)
        assertTrue(champDataReturned.version == champData.version)
        assertTrue(champDataReturned.id == champData.id)

        assertTrue(champDataDAO.exists(champData.id))
        champDataDAO.delete(champData.id)
        assertTrue(!champDataDAO.exists(champData.id))
    }

    private fun produceRandomChampData() : ChampData {
        val champData = ChampData()
        champData.blurb ="blurb"
        champData.title ="title"
        champData.name = "name"
        champData.key = "key"
        champData.id = Random().nextInt().toString()
        champData.version = "version"

        return champData
    }
}