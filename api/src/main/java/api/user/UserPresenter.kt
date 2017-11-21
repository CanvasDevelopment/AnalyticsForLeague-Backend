package api.user

import db.summoner.SummonerDao
import service_contracts.ProcessingContract

/**
 * @author Josiah Kendall
 */
class UserPresenter (private val summonerDao : SummonerDao,
                     private val serviceContract : ProcessingContract){

    fun checkIfUserExists(userName : String) : Boolean {
        return summonerDao.getSummoner(userName) != null
    }

    fun registerSummoner(userName: String) : Boolean {
        return serviceContract.createNewUser(userName) == 200
    }
}