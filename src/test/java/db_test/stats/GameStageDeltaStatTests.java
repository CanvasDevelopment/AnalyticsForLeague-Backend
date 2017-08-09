package db_test.stats;

import util.TableNames;
import db.DBHelper;
import db.stats.GameStageDeltaDAOImpl;
import model.stats.GameStageDelta;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Josiah Kendall
 */
public class GameStageDeltaStatTests {

    private GameStageDeltaDAOImpl gameStageDeltaDAO;
    private DBHelper dbHelper;

    @Before
    public void setUp() {
        dbHelper = new DBHelper();
        dbHelper.connect();
        gameStageDeltaDAO = new GameStageDeltaDAOImpl(dbHelper);
    }

    @After
    public void tearDown() {
        dbHelper.disconnect();
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

    @Test
    public void TestThatWeCanSaveAndFetchGoldPerMin() {
        GameStageDelta creepsPerMin = new GameStageDelta();
        creepsPerMin.setTenToTwenty(12.3);
        creepsPerMin.setZeroToTen(6.45);
        creepsPerMin.setTwentyToThirty(5.67);
        creepsPerMin.setThirtyToEnd(7.3333332);
        int id = gameStageDeltaDAO.saveDeltas(creepsPerMin, TableNames.GOLD_PER_MIN);
        GameStageDelta second = gameStageDeltaDAO.getGameStageDeltas(id, TableNames.GOLD_PER_MIN);
        Assert.assertEquals(creepsPerMin.getZeroToTen(), second.getZeroToTen(), 0.005);
        Assert.assertEquals(creepsPerMin.getTenToTwenty(), second.getTenToTwenty(), 0.005);
        Assert.assertEquals(creepsPerMin.getTwentyToThirty(), second.getTwentyToThirty(), 0.005);
        Assert.assertEquals(creepsPerMin.getThirtyToEnd(), second.getThirtyToEnd(), 0.005);
    }

    @Test
    public void TestThatWeCanSaveAndFetchDamageTakenPeMin() {
        GameStageDelta creepsPerMin = new GameStageDelta();
        creepsPerMin.setTenToTwenty(12.3);
        creepsPerMin.setZeroToTen(6.45);
        creepsPerMin.setTwentyToThirty(5.67);
        creepsPerMin.setThirtyToEnd(7.3333332);
        int id = gameStageDeltaDAO.saveDeltas(creepsPerMin, TableNames.DAMAGE_TAKEN_PER_MIN);
        GameStageDelta second = gameStageDeltaDAO.getGameStageDeltas(id, TableNames.DAMAGE_TAKEN_PER_MIN);
        Assert.assertEquals(creepsPerMin.getZeroToTen(), second.getZeroToTen(), 0.005);
        Assert.assertEquals(creepsPerMin.getTenToTwenty(), second.getTenToTwenty(), 0.005);
        Assert.assertEquals(creepsPerMin.getTwentyToThirty(), second.getTwentyToThirty(), 0.005);
        Assert.assertEquals(creepsPerMin.getThirtyToEnd(), second.getThirtyToEnd(), 0.005);
    }
    @Test
    public void TestThatWeCanSaveAndFetchDamageTakenDiffPerMin() {
        GameStageDelta creepsPerMin = new GameStageDelta();
        creepsPerMin.setTenToTwenty(12.3);
        creepsPerMin.setZeroToTen(6.45);
        creepsPerMin.setTwentyToThirty(5.67);
        creepsPerMin.setThirtyToEnd(7.3333332);
        int id = gameStageDeltaDAO.saveDeltas(creepsPerMin, TableNames.DAMAGE_TAKEN_DIFF_PER_MIN);
        GameStageDelta second = gameStageDeltaDAO.getGameStageDeltas(id, TableNames.DAMAGE_TAKEN_DIFF_PER_MIN);
        Assert.assertEquals(creepsPerMin.getZeroToTen(), second.getZeroToTen(), 0.005);
        Assert.assertEquals(creepsPerMin.getTenToTwenty(), second.getTenToTwenty(), 0.005);
        Assert.assertEquals(creepsPerMin.getTwentyToThirty(), second.getTwentyToThirty(), 0.005);
        Assert.assertEquals(creepsPerMin.getThirtyToEnd(), second.getThirtyToEnd(), 0.005);
    }

    @Test
    public void TestThatWeCanSaveAndFetchXpDiffPerMin() {
        GameStageDelta creepsPerMin = new GameStageDelta();
        creepsPerMin.setTenToTwenty(12.3);
        creepsPerMin.setZeroToTen(6.45);
        creepsPerMin.setTwentyToThirty(5.67);
        creepsPerMin.setThirtyToEnd(7.3333332);
        int id = gameStageDeltaDAO.saveDeltas(creepsPerMin, TableNames.XP_DIFF_PER_MIN);
        GameStageDelta second = gameStageDeltaDAO.getGameStageDeltas(id, TableNames.XP_DIFF_PER_MIN);
        Assert.assertEquals(creepsPerMin.getZeroToTen(), second.getZeroToTen(), 0.005);
        Assert.assertEquals(creepsPerMin.getTenToTwenty(), second.getTenToTwenty(), 0.005);
        Assert.assertEquals(creepsPerMin.getTwentyToThirty(), second.getTwentyToThirty(), 0.005);
        Assert.assertEquals(creepsPerMin.getThirtyToEnd(), second.getThirtyToEnd(), 0.005);
    }

    @Test
    public void TestThatWeCanSaveAndFetchXpPerMin() {
        GameStageDelta creepsPerMin = new GameStageDelta();
        creepsPerMin.setTenToTwenty(12.3);
        creepsPerMin.setZeroToTen(6.45);
        creepsPerMin.setTwentyToThirty(5.67);
        creepsPerMin.setThirtyToEnd(7.3333332);
        int id = gameStageDeltaDAO.saveDeltas(creepsPerMin, TableNames.XP_PER_MIN);
        GameStageDelta second = gameStageDeltaDAO.getGameStageDeltas(id, TableNames.XP_PER_MIN);
        Assert.assertEquals(creepsPerMin.getZeroToTen(), second.getZeroToTen(), 0.005);
        Assert.assertEquals(creepsPerMin.getTenToTwenty(), second.getTenToTwenty(), 0.005);
        Assert.assertEquals(creepsPerMin.getTwentyToThirty(), second.getTwentyToThirty(), 0.005);
        Assert.assertEquals(creepsPerMin.getThirtyToEnd(), second.getThirtyToEnd(), 0.005);
    }
}
