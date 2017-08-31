package db.stats

import model.stats.CreepsPerMin

/**
 * @author Josiah Kendall
 * *
 * * The Data access object for creeps per min.
 */
interface CreepsPerMinDAO {

    fun saveCreepsPerMin(creepsPerMin: CreepsPerMin): Long
    fun deleteCreepsPerMin(creepsPerMinId: Long)
    fun getCreepsPerMin(creepsPerMinId: Long): CreepsPerMin
}
