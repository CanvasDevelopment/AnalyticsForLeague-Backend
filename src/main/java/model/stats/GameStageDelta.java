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

    @com.google.gson.annotations.SerializedName("0-10")
    public double zeroToTen;
    @com.google.gson.annotations.SerializedName("10-20")
    public double tenToTwenty;
    @com.google.gson.annotations.SerializedName("20-30")
    public double twentyToThirty;

    public double thirtyToEnd;


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
    public void setZeroToTen(double zeroToTen) {
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
    public void setTenToTwenty(double tenToTwenty) {
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
    public void setTwentyToThirty(double twentyToThirty) {
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
    public void setThirtyToEnd(double thirtyToEnd) {
        this.thirtyToEnd = thirtyToEnd;
    }
}
