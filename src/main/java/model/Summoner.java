package model;

/**
 * @author Josiah Kendall
 *
 * Pojo for a summoner.
 */
public class Summoner {

    private long id;
    private long accountId;
    private String name;
    private int profileIconId;
    private int summonerLevel;
    private long revisionDate;

    public int getProfileIconId() {
        return profileIconId;
    }

    public int getSummonerLevel() {
        return summonerLevel;
    }

    public long getAccountId() {
        return accountId;
    }

    public long getId() {
        return id;
    }

    public long getRevisionDate() {
        return revisionDate;
    }

    public String getName() {
        return name;
    }

    public void setAccountId(long accountId) {
        this.accountId = accountId;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setProfileIconId(int profileIconId) {
        this.profileIconId = profileIconId;
    }

    public void setRevisionDate(long revisionDate) {
        this.revisionDate = revisionDate;
    }

    public void setSummonerLevel(int summonerLevel) {
        this.summonerLevel = summonerLevel;
    }

    @Override
    public String toString() {
        String result = String.format("Id: %s\nAccountId: %s\nName: %s\nProfileIconId: %s\nSummonerLevel: %s\nRevisionDate: %s",
                id, accountId, name, profileIconId, summonerLevel, revisionDate);
        return result;
    }
}
