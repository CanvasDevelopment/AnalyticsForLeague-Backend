package api.stat.analysis

import api.stat.analysis.model.AnalysisStatCardSkeleton
import util.Constant.AnalysisCardType.FULL_STAT
import util.Constant.AnalysisCardType.HALF_STAT
import util.Constant.StatTypes.CREEPS
import util.Constant.StatTypes.DAMAGE_DEALT
import util.Constant.StatTypes.GOLD
import util.Constant.StatTypes.XP
import util.Constant.LANE.ADC
import util.Constant.LANE.JUNGLE
import util.Constant.LANE.MID
import util.Constant.LANE.SUPPORT
import util.Constant.LANE.TOP
import util.Constant.StatTypes.WARDS_KILLED
import util.Constant.StatTypes.WARDS_PLACED

/**
 * @author Josiah Kendall
 */
class DefaultStatTypes {

    private val defaultNumberOfGames = 20

    private fun produceFullStatCard(summonerId: String, champId: Int, role: String, statType: String, statName : String) : AnalysisStatCardSkeleton {
        return AnalysisStatCardSkeleton(FULL_STAT,
                "fullStatCard/$summonerId/$defaultNumberOfGames/$role/$champId/$statType",
                "",
                statName)
    }

    private fun produceFullStatCard(summonerId: String, role: String, statType: String, statName: String) : AnalysisStatCardSkeleton {
        return AnalysisStatCardSkeleton(FULL_STAT,
                "fullStatCard/$summonerId/$defaultNumberOfGames/$role/$statType",
                "",
                statName)
    }

    private fun produceHalfStatCard(summonerId: String, role: String, champId: Int, statType: String, statName: String) : AnalysisStatCardSkeleton {
        return AnalysisStatCardSkeleton(
                HALF_STAT,
                "halfStatCard/$summonerId/$defaultNumberOfGames/$role/$champId/$statType",
                "",
                statName
        )
    }

    private fun produceHalfStatCard(summonerId: String, role: String, statType: String, statName: String) : AnalysisStatCardSkeleton {
        return AnalysisStatCardSkeleton(
                HALF_STAT,
                "halfStatCard/$summonerId/$defaultNumberOfGames/$role/$statType",
                "",
                statName
        )
    }

    fun getDefaultStats(summonerId: String, role : String) : ArrayList<AnalysisStatCardSkeleton> {

        val creepsCard = produceFullStatCard(summonerId, role, CREEPS, "Creeps")
        val damage = produceFullStatCard(summonerId, role, DAMAGE_DEALT, "Damage Dealt")
        val xp = produceFullStatCard(summonerId, role, XP, "XP")
        val gold= produceFullStatCard(summonerId, role, GOLD, "Gold")

        val kda = AnalysisStatCardSkeleton(
                FULL_STAT,
                "kda/$summonerId/$defaultNumberOfGames/$role",
                "",
                "Kills / Deaths / Assists"
        )

        val wardsPlaced = produceFullStatCard(summonerId, role, WARDS_PLACED, "Wards Placed")
        val wardsKilled = produceFullStatCard(summonerId, role, WARDS_KILLED, "Wards Killed")

        val result = ArrayList<AnalysisStatCardSkeleton>()

//        result.add(kda)
        result.add(creepsCard)
        result.add(damage)
        result.add(xp)
        result.add(gold)
//        result.add(wardsPlaced)
//        result.add(wardsKilled)

        return result
    }

    fun getDefaultStats(summonerId: String, champId: Int, role : String) : ArrayList<AnalysisStatCardSkeleton> {

        val creepsCard = produceFullStatCard(summonerId, champId, role, CREEPS, "Creeps Per Minute")
        val damage = produceFullStatCard(summonerId, champId, role, DAMAGE_DEALT, "Damage Dealt Per Minute")
        val xp = produceFullStatCard(summonerId, champId, role, XP, "XP Per Minute")
        val gold= produceFullStatCard(summonerId, champId, role, GOLD, "Gold Per Minute")

        val kda = AnalysisStatCardSkeleton(
                FULL_STAT,
                "kda/$summonerId/$defaultNumberOfGames/$role/$champId",
                "", // detail url can probably be done dynamically
                "Kills / Deaths / Assists"
        )

        val wardsPlaced = produceFullStatCard(summonerId, champId, role, WARDS_PLACED, "Wards Placed")
        val wardsKilled = produceFullStatCard(summonerId, champId, role, WARDS_KILLED, "Wards Killed")

        val result = ArrayList<AnalysisStatCardSkeleton>()

        result.add(kda)
        result.add(creepsCard)
        result.add(damage)
        result.add(xp)
        result.add(gold)
        result.add(wardsPlaced)
        result.add(wardsKilled)

        return result
    }




    //

}