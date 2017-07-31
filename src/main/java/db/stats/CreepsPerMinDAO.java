package db.stats;

import model.stats.CreepsPerMin;

/**
 * @author Josiah Kendall
 *
 * The Data access object for creeps per min.
 */
public interface CreepsPerMinDAO {

    int saveCreepsPerMin(CreepsPerMin creepsPerMin);
    void deleteCreepsPerMin(long creepsPerMinId);
    CreepsPerMin getCreepsPerMin(long creepsPerMinId);

}
