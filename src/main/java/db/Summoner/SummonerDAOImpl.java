package db.summoner;

import db.DBHelper;
import model.Summoner;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Logger;

/**
 * @author Josiah Kendall
 */
public class SummonerDAOImpl implements SummonerDAO {
    private static final Logger log = Logger.getLogger(SummonerDAO.class.getName());

    private DBHelper dbHelper;

    /**
     *
     * @param dbHelper
     */
    public SummonerDAOImpl(DBHelper dbHelper) {
        this.dbHelper = dbHelper;
    }

    /**
     * Save a summoner to the summoner table.
     * @param summoner The summoner to save.
     * @return -1 if save was unsuccessful, else return the result.
     */
    @Override
    public int saveSummoner(Summoner summoner) {
        log.info("Saving summoner : " + summoner.toString());
        String queryString = String.format(
                "Insert into %s ("
                        + "%s,"
                        + "%s,"
                        + "%s,"
                        + "%s,"
                        + "%s,"
                        + "%s)"
                        + " values (%d, %d, '%s', %d, %d, %d)",
                SUMMONER_TABLE,
                ID,
                ACCOUNT_ID,
                SUMMONER_NAME,
                PROFILE_ICON_ID,
                SUMMONER_LEVEL,
                REVISION_DATE,
                summoner.getId(),
                summoner.getAccountId(),
                summoner.getName(),
                summoner.getProfileIconId(),
                summoner.getSummonerLevel(),
                summoner.getRevisionDate()
        );

        try {
            int result = dbHelper.ExecuteSqlScript(queryString);
            log.info("Successfully saved with Id : " + result);
            return result;
        } catch (SQLException ex) {
            log.severe(ex.getMessage());
        } catch (IllegalStateException ex) {
            log.severe(ex.getLocalizedMessage());
        }

        return -1;
    }

    @Override
    public void deleteSummoner(long summonerId) {
        log.info("Attempting to delete summoner with id : " + summonerId);
        String queryString = String.format("DELETE from %s WHERE %s = %s", SUMMONER_TABLE, ID, summonerId);
        try {
            dbHelper.ExecuteSqlScript(queryString);
        } catch (SQLException e) {
            log.info("An error was found deleting summoner with id: " + summonerId);
            log.severe(e.getMessage());
        }
    }

    @Override
    public Summoner getSummoner(long summonerId) {
        log.info("Attempting to find summoner with Id : " + summonerId);
        String queryString = String.format("SELECT * FROM %s WHERE %s = %s",SUMMONER_TABLE, ID, summonerId);
        try {
            ResultSet resultSet = dbHelper.ExecuteSqlQuery(queryString);
            if (resultSet.next()) {
                Summoner summoner = new Summoner();
                summoner.setId(summonerId);
                summoner.setName(resultSet.getString("SummonerName"));
                summoner.setProfileIconId(resultSet.getInt("ProfileIconId"));
                summoner.setSummonerLevel(resultSet.getInt("SummonerLevel"));
                summoner.setAccountId(resultSet.getLong("AccountId"));
                summoner.setRevisionDate(resultSet.getLong("RevisionDate"));

                log.info("Successfully loaded summoner : \n" + summoner.toString());
                return summoner;
            }
        } catch (SQLException ex) {
            log.severe(ex.getMessage());
        }

        return null;
    }
}
