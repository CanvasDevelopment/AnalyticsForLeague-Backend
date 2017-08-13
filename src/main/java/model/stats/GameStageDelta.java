package model.stats;

import com.google.appengine.repackaged.com.google.gson.annotations.Expose;
import com.google.appengine.repackaged.com.google.gson.annotations.SerializedName;

/**
 * @author Josiah Kendall
 *
 * This is a game stage delta. It holds values for the various stages of a game of league.
 * Used by many different stats, such as Creeps, gold, xp, etc...
 */
public class GameStageDelta {
    private long id;

    @SerializedName("zeroToTen")
    @Expose
    private Double zeroToTen;
    @SerializedName("tenToTwenty")
    @Expose
    private Double tenToTwenty;
    @SerializedName("twentyToThirty")
    @Expose
    private Double twentyToThirty;
    @SerializedName("thirtyToEnd")
    @Expose
    private Double thirtyToEnd;


    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }
    /**
     *
     * @return
     * The zeroToTen
     */
    public double getZeroToTen() {
        return zeroToTen;
    }

    /**
     *
     * @param zeroToTen
     * The zeroToTen
     */
    public void setZeroToTen(Double zeroToTen) {
        this.zeroToTen = zeroToTen;
    }

    /**
     *
     * @return
     * The tenToTwenty
     */
    public double getTenToTwenty() {
        return tenToTwenty;
    }

    /**
     *
     * @param tenToTwenty
     * The tenToTwenty
     */
    public void setTenToTwenty(Double tenToTwenty) {
        this.tenToTwenty = tenToTwenty;
    }

    /**
     *
     * @return
     * The twentyToThirty
     */
    public double getTwentyToThirty() {
        return twentyToThirty;
    }

    /**
     *
     * @param twentyToThirty
     * The twentyToThirty
     */
    public void setTwentyToThirty(Double twentyToThirty) {
        this.twentyToThirty = twentyToThirty;
    }

    /**
     *
     * @return
     * The thirtyToEnd
     */
    public double getThirtyToEnd() {
        return thirtyToEnd;
    }

    /**
     *
     * @param thirtyToEnd
     * The thirtyToEnd
     */
    public void setThirtyToEnd(Double thirtyToEnd) {
        this.thirtyToEnd = thirtyToEnd;
    }
}
