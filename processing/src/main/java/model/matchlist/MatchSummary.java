package model.matchlist;

/**
 * @author Josiah Kendall
 *
 * The pojo for the list of items that gets returned in the matchList object.
 */
public class MatchSummary {
    private int id;
    private String platformId;
    private long gameId;
    private int champion;
    private int queue;
    private int season;
    private long timestamp;
    private String role;
    private String lane;
    private long summonerId;

    public MatchSummary() {
        // ?? why is this here?
    }

    public MatchSummary(int id,
                        String platformId,
                        long gameId,
                        int champion,
                        int queue,
                        int season,
                        long timestamp,
                        String role,
                        String lane,
                        long summonerId) {
        this.champion = champion;
        this.gameId = gameId;
        this.queue = queue;
        this.id = id;
        this.platformId = platformId;
        this.season = season;
        this.timestamp = timestamp;
        this.role = role;
        this.lane = lane;
        this.summonerId = summonerId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPlatformId() {
        return platformId;
    }

    public void setPlatformId(String platformId) {
        this.platformId = platformId;
    }

    public void setChampion(int champion) {
        this.champion = champion;
    }

    public void setGameId(long gameId) {
        this.gameId = gameId;
    }

    public void setLane(String lane) {
        this.lane = lane;
    }

    public void setQueue(int queue) {
        this.queue = queue;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setSeason(int season) {
        this.season = season;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public int getChampion() {
        return champion;
    }

    public int getQueue() {
        return queue;
    }

    public int getSeason() {
        return season;
    }

    public long getGameId() {
        return gameId;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public String getLane() {
        return lane;
    }

    public String getRole() {
        return role;
    }

    public long getSummonerId() {
        return summonerId;
    }

    public void setSummonerId(long summonerId) {
        this.summonerId = summonerId;
    }

}
