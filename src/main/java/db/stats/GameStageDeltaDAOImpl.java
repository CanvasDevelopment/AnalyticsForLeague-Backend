package db.stats;

import db.DBHelper;
import model.stats.CreepsPerMin;
import model.stats.GameStageDelta;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Josiah Kendall
 */
public class GameStageDeltaDAOImpl implements GameStageDeltasDAO{

    private DBHelper dbHelper;

    public GameStageDeltaDAOImpl(DBHelper dbHelper) {
        this.dbHelper = dbHelper;
    }

    @Override
    public int saveDeltas(GameStageDelta gameStageDelta, String tableName) {
        try {
            String queryString = String.format(
                    "Insert into %s ("
                            + "ZeroToTen,"
                            + "TenToTwenty,"
                            + "TwentyToThirty,"
                            + "ThirtyToEnd) values (%s, %s, %s, %s)",
                    tableName,
                    gameStageDelta.getZeroToTen(),
                    gameStageDelta.getTenToTwenty(),
                    gameStageDelta.getTwentyToThirty(),
                    gameStageDelta.getThirtyToEnd()
            );
            return dbHelper.ExecuteSqlScript(queryString);
        } catch (SQLException | IllegalStateException | NullPointerException ex) {
            Logger.getLogger(CreepsPerMinDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        }

        return -1;
    }

    @Override
    public void deleteDeltas(long gameStageDeltasId, String tableName) {

    }

    @Override
    public GameStageDelta getGameStageDeltas(long gameStageDeltasId, String tableName) {
        try {
            // #sorrynotsorry
            String query = "SELECT * from "+tableName+" WHERE Id = " + gameStageDeltasId;
            ResultSet resultSet = dbHelper.ExecuteSqlQuery(query);
            if (resultSet.next()) {
                GameStageDelta gameStageDelta = new GameStageDelta();
                gameStageDelta.setId(gameStageDeltasId);
                gameStageDelta.setZeroToTen(resultSet.getDouble("ZeroToTen"));
                gameStageDelta.setTenToTwenty(resultSet.getDouble("TenToTwenty"));
                gameStageDelta.setTwentyToThirty(resultSet.getDouble("TwentyToThirty"));
                gameStageDelta.setThirtyToEnd(resultSet.getDouble("ThirtyToEnd"));

                return gameStageDelta;
            }
        } catch (SQLException | IllegalStateException ex) {
            Logger.getLogger(CreepsPerMinDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        }

        return null;
    }
}
