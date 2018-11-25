import com.google.appengine.repackaged.com.google.gson.Gson
import db.champion.ChampData
import db.champion.ChampDataDAO
import java.io.File
import java.io.FileOutputStream
import java.util.zip.ZipEntry
import java.io.FileInputStream
import java.util.zip.ZipInputStream
import java.io.BufferedInputStream
import java.net.URL
import java.util.zip.ZipFile


class ChampDataProcessing(private val champDataDAO: ChampDataDAO) {

    val downloadFolder : String = "/Users/emilepest/Downloads/"
    val jsonLocaton : String = "/8.16.1/data/en_US/champion.json"
    val extractedFolder = "extracted"
    val fileName = "${downloadFolder}champs.json"


    fun start(version : String) {
        val url = "http://ddragon.leagueoflegends.com/cdn/$version/data/en_US/champion.json"
        val downloadFileLocation = downloadFile(url)
//        unzipFile(downloadFileLocation)
        processChampDataFile(downloadFileLocation)
    }

    private fun downloadFile(url : String) : String {

        var input: BufferedInputStream? = null
        var fout: FileOutputStream? = null
        try {
            input = BufferedInputStream(URL(url).openStream())
            fout = FileOutputStream(fileName)

            val data = ByteArray(1024)
            var count: Int
            do {
                count = input.read(data, 0, 1024)
                fout.write(data, 0, count)
            } while (count != -1)
        } catch (error : IndexOutOfBoundsException) {
            // do nothing, I think its sweet.
        } finally {
            input?.close()
            fout?.close()
        }
        return fileName
    }

    private fun unzipFile(downloadFile : String) {
        val buffer = ByteArray(1024)
        val zip = ZipFile(downloadFile)
        val zis = ZipInputStream(BufferedInputStream(FileInputStream(downloadFile)))
        var zipEntry: ZipEntry? = zis.nextEntry
        while (zipEntry != null) {
            val fileName = zipEntry.name
            val newFile = File("$extractedFolder/$fileName")
            val fos = FileOutputStream(newFile)

            var len : Int
            do {
                len = zis.read(buffer)
                fos.write(buffer, 0, len)
            } while (len > 0)
            fos.close()
            zipEntry = zis.nextEntry
        }
        zis.closeEntry()
        zis.close()
    }
//    fun saveAndExtractData

    fun processChampDataFile(jsonDataPath : String) {
        // path to data
        // open as string
        var result = ""
        File(jsonDataPath).forEachLine { result += it }
        // parse it into a jsonWrapper
        val gson = Gson()
        val champWrapper = gson.fromJson(result, ChampWrapper::class.java)
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