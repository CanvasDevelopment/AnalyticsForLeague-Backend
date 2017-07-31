package db_test.stats;

import db.DBHelper;
import db.stats.CreepsPerMinDAOImpl;
import model.stats.CreepsPerMin;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Josiah Kendall
 */
public class CreepsPerMin_Tests {
    DBHelper dbHelper;
    CreepsPerMinDAOImpl creepsPerMinDAO;
    @Before
    public void setUp() {
        dbHelper = new DBHelper();
        dbHelper.Connect();
        creepsPerMinDAO = new CreepsPerMinDAOImpl(dbHelper);
    }

    @After
    public void tearDown() {
        dbHelper.Disconnect();
    }

    @Test
    public void EnsureThatWeCanSaveASummonerThenFetchItById() {

        CreepsPerMin creepsPerMin = new CreepsPerMin();
        creepsPerMin.setTenToTwenty(12.3);
        creepsPerMin.setZeroToTen(6.45);
        creepsPerMin.setTwentyToThirty(5.67);
        creepsPerMin.setThirtyToEnd(7.3333332);

        int id = creepsPerMinDAO.saveCreepsPerMin(creepsPerMin);
        CreepsPerMin creepsPerMin1 = creepsPerMinDAO.getCreepsPerMin(id);
        Assert.assertTrue(creepsPerMin.getZeroToTen() == creepsPerMin1.getZeroToTen());
        Assert.assertTrue(creepsPerMin.getTenToTwenty() == creepsPerMin1.getTenToTwenty());
        Assert.assertTrue(creepsPerMin.getTwentyToThirty() == creepsPerMin1.getTwentyToThirty());
        Assert.assertEquals(creepsPerMin.getThirtyToEnd(), creepsPerMin1.getThirtyToEnd(), 0.05);
    }
}
