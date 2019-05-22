package api.champ

import com.google.gson.Gson
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class ChampDataProcessing(private val champDataDAO: ChampDataDAO) {

    val downloadFolder : String = "/Users/emilepest/Downloads/"
    val jsonLocaton : String = "/8.16.1/data/en_US/champion.json"
    val extractedFolder = "extracted"
    val fileName = "${downloadFolder}champs.json"


    fun start(version : String) {
        val url = "http://ddragon.leagueoflegends.com/cdn/$version/data/en_US/champion.json"
        val stringValue = downloadFile(url)
//        unzipFile(downloadFileLocation)
        processChampDataFile(stringValue)
    }

    private fun downloadFile(urlString : String) : String {
        val gson = Gson()
        val url = URL(urlString)
        val conn = url.openConnection() as HttpURLConnection
        conn.doOutput = true
        conn.requestMethod = "GET"
        conn.connectTimeout = 10000

        val respCode = conn.responseCode
        if (respCode == HttpURLConnection.HTTP_OK) {
            val response = StringBuffer()
            val reader = BufferedReader(InputStreamReader(conn.inputStream))

            // Parse our response
            val lines = reader.readLines()
            for(line in lines) {
                response.append(line)
            }
            reader.close()
            return response.toString()
        }
        return ""
    }


//    fun saveAndExtractData

    fun processChampDataFile(stringValue : String) {
        // path to data
        // open as string

        val gson = Gson()
        val champWrapper = gson.fromJson(stringValue, ChampWrapper::class.java)
        // now run through each of the items and save them to the database
        saveChamps(champWrapper.data!!)
    }

    private fun saveChamps(champData : Map<String, ChampData> ) {
        val champKeyIterator = champData.keys.iterator()
        while (champKeyIterator.hasNext()) {
            val champData = champData[champKeyIterator.next()]
            champDataDAO.saveChampData(champData!!)
        }

        // for champ
    }
}