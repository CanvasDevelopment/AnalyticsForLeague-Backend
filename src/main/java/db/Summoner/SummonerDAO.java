package db.summoner;

import model.Summoner;

/**
 * @author Josiah Kendall
 */
public interface SummonerDAO {
    public static final String SUMMONER_TABLE = "Summoner";
    public static final String ID = "Id";
    public static final String ACCOUNT_ID = "AccountId";
    public static final String SUMMONER_NAME = "SummonerName";
    public static final String SUMMONER_LEVEL = "SummonerLevel";
    public static final String PROFILE_ICON_ID = "ProfileIconId";
    public static final String REVISION_DATE = "RevisionDate";

    long saveSummoner(Summoner summoner);
    void deleteSummoner(long summonerId);
    Summoner getSummoner(long summonerId);
}
