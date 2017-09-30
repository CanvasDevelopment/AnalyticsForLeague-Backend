package database.summoner;

import model.SummonerDetails;

/**
 * @author Josiah Kendall
 */
public interface SummonerDAOContract {
    public static final String SUMMONER_TABLE = "Summoner";
    public static final String ID = "Id";
    public static final String ACCOUNT_ID = "AccountId";
    public static final String SUMMONER_NAME = "SummonerName";
    public static final String SUMMONER_LEVEL = "SummonerLevel";
    public static final String PROFILE_ICON_ID = "ProfileIconId";
    public static final String REVISION_DATE = "RevisionDate";

    long saveSummoner(SummonerDetails summoner);
    void deleteSummoner(long summonerId);
    SummonerDetails getSummoner(long summonerId);
}
