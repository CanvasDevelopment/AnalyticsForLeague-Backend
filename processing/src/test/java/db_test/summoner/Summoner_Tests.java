package db_test.summoner;

import db.requests.DBHelper;
import db.summoner.SummonerDAOContractImpl;
import model.Summoner;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Josiah Kendall
 *
 * Read/write/delete tests for summoner DAO
 */
public class Summoner_Tests {

    DBHelper dbHelper;
    SummonerDAOContractImpl summonerDAO;
    @Before
    public void setUp() {
        dbHelper = new DBHelper();
        dbHelper.connect();
        summonerDAO = new SummonerDAOContractImpl(dbHelper);
    }

    @After
    public void tearDown() {
        dbHelper.disconnect();
    }

    @Test
    public void EnsureThatWeCanSaveASummonerThenFetchItById() {

        SummonerDAOContractImpl summonerDAO = new SummonerDAOContractImpl(dbHelper);
        Summoner summoner = new Summoner();
        summoner.setId(1);
        summoner.setAccountId(1);
        summoner.setSummonerLevel(30);
        summoner.setProfileIconId(3);
        summoner.setName("JOSIAH");
        summoner.setRevisionDate(12345676);
        long result = summonerDAO.saveSummoner(summoner);
        Summoner summoner2 = summonerDAO.getSummoner(summoner.getId());
        Assert.assertTrue(summoner2.getName().equals(summoner.getName()));
        Assert.assertTrue(summoner2.getAccountId() == summoner.getAccountId());
        Assert.assertTrue(summoner2.getProfileIconId() == summoner.getProfileIconId());
        Assert.assertTrue(summoner2.getRevisionDate() == summoner.getRevisionDate());
        Assert.assertTrue(summoner2.getSummonerLevel() == summoner.getSummonerLevel());
    }

    @Test
    public void EnsureThatWeCanDeleteASummonerById() {
        Summoner summoner = new Summoner();
        summoner.setId(1);
        summoner.setAccountId(1);
        summoner.setSummonerLevel(30);
        summoner.setProfileIconId(3);
        summoner.setName("JOSIAH");
        summoner.setRevisionDate(12345676);
        long result = summonerDAO.saveSummoner(summoner);
        Summoner summoner2 = summonerDAO.getSummoner(summoner.getId());
        Assert.assertTrue(summoner2!= null);
        summonerDAO.deleteSummoner(summoner.getId());
        summoner2 = summonerDAO.getSummoner(summoner.getId());
        Assert.assertTrue(summoner2 == null);
    }
}
