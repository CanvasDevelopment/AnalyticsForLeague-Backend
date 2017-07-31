package db_test.stats;

import Util.TableNames;
import db.DBHelper;
import db.stats.GameStageDeltaDAOImpl;
import model.stats.CreepsPerMin;
import model.stats.GameStageDelta;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Josiah Kendall
 */
public class CsDiffPerMin_Tests {

    private GameStageDeltaDAOImpl gameStageDeltaDAO;
    private DBHelper dbHelper;

    @Before
    public void setUp() {
        dbHelper = new DBHelper();
        dbHelper.Connect();
        gameStageDeltaDAO = new GameStageDeltaDAOImpl(dbHelper);
    }

    @After
    public void tearDown() {
        dbHelper.Disconnect();
    }

    @Test
    public void TestThatWeCanSaveAndFetchCsDiffPerMin() {
        GameStageDelta creepsPerMin = new GameStageDelta();
        creepsPerMin.setTenToTwenty(12.3);
        creepsPerMin.setZeroToTen(6.45);
        creepsPerMin.setTwentyToThirty(5.67);
        creepsPerMin.setThirtyToEnd(7.3333332);
        int id = gameStageDeltaDAO.saveDeltas(creepsPerMin, TableNames.CS_DIFF_PER_MIN);
        GameStageDelta second = gameStageDeltaDAO.getGameStageDeltas(id, TableNames.CS_DIFF_PER_MIN);
        Assert.assertEquals(creepsPerMin.getZeroToTen(), second.getZeroToTen(), 0.005);
        Assert.assertEquals(creepsPerMin.getTenToTwenty(), second.getTenToTwenty(), 0.005);
        Assert.assertEquals(creepsPerMin.getTwentyToThirty(), second.getTwentyToThirty(), 0.005);
        Assert.assertEquals(creepsPerMin.getThirtyToEnd(), second.getThirtyToEnd(), 0.005);
    }
}
