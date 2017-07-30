package db.Summoner;

import model.Summoner;

/**
 * @author Josiah Kendall
 */
public interface SummonerDAO {
    int saveSummoner(Summoner summoner);
    void deleteSummoner(long summonerId);
    Summoner getSummoner(long summonerId);
}
