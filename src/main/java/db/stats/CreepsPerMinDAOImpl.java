package db.stats;

import db.DBHelper;
import model.stats.CreepsPerMin;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Josiah Kendall
 */
public class CreepsPerMinDAOImpl implements CreepsPerMinDAO {

    private DBHelper dbHelper;

    public CreepsPerMinDAOImpl(DBHelper dbHelper) {
        this.dbHelper = dbHelper;
    }

    @Override
    public int saveCreepsPerMin(CreepsPerMin creepsPerMin) {

        try {
            String queryString = String.format(
                    "Insert into creepspermindeltas ("
                            + "ZeroToTen,"
                            + "TenToTwenty,"
                            + "TwentyToThirty,"
                            + "ThirtyToEnd) values (%s, %s, %s, %s)",
                    creepsPerMin.getZeroToTen(),
                    creepsPerMin.getTenToTwenty(),
                    creepsPerMin.getTwentyToThirty(),
                    creepsPerMin.getThirtyToEnd()
            );
            return dbHelper.ExecuteSqlScript(queryString);
        } catch (SQLException | IllegalStateException | NullPointerException ex) {
            Logger.getLogger(CreepsPerMinDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        }

        return -1;
    }

    @Override
    public void deleteCreepsPerMin(long creepsPerMinId) {
        // not actually sure where this will be ever used so im going to leave it for the time being. if use cases arive it
        // will be implemented
    }

    @Override
    public CreepsPerMin getCreepsPerMin(long creepsPerMinId) {
        try {
            String query = "SELECT * from creepspermindeltas WHERE Id = " + creepsPerMinId;
            ResultSet resultSet = dbHelper.ExecuteSqlQuery(query);
            if (resultSet.next()) {
                CreepsPerMin cmpd = new CreepsPerMin();
                cmpd.setId(creepsPerMinId);
                cmpd.setZeroToTen(resultSet.getDouble("ZeroToTen"));
                cmpd.setTenToTwenty(resultSet.getDouble("TenToTwenty"));
                cmpd.setTwentyToThirty(resultSet.getDouble("TwentyToThirty"));
                cmpd.setThirtyToEnd(resultSet.getDouble("ThirtyToEnd"));

                return cmpd;
            }
        } catch (SQLException | IllegalStateException ex) {
            Logger.getLogger(CreepsPerMinDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        }

        return null;
    }
}
