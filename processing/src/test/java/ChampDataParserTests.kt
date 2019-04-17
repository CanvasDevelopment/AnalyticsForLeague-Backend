import com.google.appengine.repackaged.com.google.gson.Gson

import db.champion.ChampData
import db.champion.ChampDataDAO
import db.requests.DBHelper
import org.junit.Before
import org.junit.Test
import java.io.File
import java.sql.ResultSet

class ChampDataParserTests {

    val dbHelper : DBHelper = DBHelper()
    val champDataDAO : ChampDataDAO = ChampDataDAO(dbHelper)
    val champDataProcessing : ChampDataProcessing = ChampDataProcessing(champDataDAO)

    @Test
    fun testThatWeCanOpenChampJson() {
       // path to data
        val jsonDataPath = "/Users/emilepest/Downloads/dragontail-8.16.1/8.16.1/data/en_US/champion.json"
        // open as string
        var result = ""
        File(jsonDataPath).forEachLine { result += it }
        assert(result[0] == '{')
        // parse it
        // test thatthe data is there
    }

    @Before
    fun doSetUp() {
        dbHelper.connect()
//        dbHelper.executeSQLScript("delete from ChampData")
    }

    @Test
    fun testThatWeCanParseData() {

        // path to data
        val jsonDataPath = "/Users/emilepest/Downloads/dragontail-8.16.1/8.16.1/data/en_US/champion.json"
        // open as string
        var result = ""
        File(jsonDataPath).forEachLine { result += it }
        // parse it into a jsonWrapper
        val gson = Gson()
        val champWrapper = gson.fromJson(result, ChampWrapper::class.java)
        // now run through each of the items and save them to the database
        saveChamps(champWrapper.data!!)
        val summonerId = "-1L"
        // test thatthe data is there
        val iterator = champWrapper.data!!.keys.iterator()
        while (iterator.hasNext()) {
            val champData = champWrapper.data!![iterator.next()]
            val loadedChampData = champDataDAO.loadChampData(champData!!.id, summonerId)
            assert(loadedChampData.id == champData.id)
        }

        //
//        fetchChampFromDb("Aatrox")
    }

    @Test
    fun testThatWeCanRunEntireProcess() {
        val url = "8.17.1"
        champDataProcessing.start(url)
    }

    private fun saveChamps(champData : Map<String, ChampData> ) {

        val champKeyIterator = champData.keys.iterator()
        while (champKeyIterator.hasNext()) {
            val champData = champData[champKeyIterator.next()]
            champDataDAO.saveChampData(champData!!)
        }
    }


}