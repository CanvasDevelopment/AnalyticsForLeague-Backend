package domain_tests

import application.domain.ChampControl
import com.github.salomonbrys.kodein.instance
import db.champion.ChampionDAO
import di.KodeinManager
import network.RequestHandler
import network.riotapi.ChampionService
import org.junit.Test
import util.RIOT_API_KEY
import java.io.BufferedReader
import java.io.FileInputStream
import java.io.InputStreamReader


/**
 * @author Josiah Kendall
 */
class ChampionControlTests{

    private val km = KodeinManager()
    private val champService = km.kodein.instance<ChampionService>()
    private val champDao = km.kodein.instance<ChampionDAO>()
    private val requestHandler = km.kodein.instance<RequestHandler>()

    /**
     * This test will take hours to complete :P
     */
    @Test
    fun `Make sure we can fetch and save all champions`() {
        val champController = ChampControl(champService, champDao, requestHandler)
        champController.fetchAndSaveAllChamps()
        assert( champDao.getChampion(62.toLong())?.name == "Wukong")
    }


    private fun getChampList(): String {
        val inputs = FileInputStream("C:\\Users\\jek40\\Desktop\\analyticsforleague.com\\afl_backend\\processing\\src\\main\\resources\\json\\champList.json")
        val buf = BufferedReader(InputStreamReader(inputs))
        var line = buf.readLine()
        val sb = StringBuilder()
        while (line != null) {
            sb.append(line).append("\n")
            line = buf.readLine()
        }

        return sb.toString()
    }
}