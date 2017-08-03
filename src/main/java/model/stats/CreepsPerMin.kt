package model.stats

import com.google.appengine.repackaged.com.google.gson.annotations.Expose
import com.google.appengine.repackaged.com.google.gson.annotations.SerializedName

/**
 * @author Josiah Kendall
 */
class CreepsPerMin {
    var id: Long = 0

    @SerializedName("zeroToTen")
    @Expose
    private var zeroToTen: Double? = null
    @SerializedName("tenToTwenty")
    @Expose
    private var tenToTwenty: Double? = null
    @SerializedName("twentyToThirty")
    @Expose
    private var twentyToThirty: Double? = null
    @SerializedName("thirtyToEnd")
    @Expose
    private var thirtyToEnd: Double? = null

    /**

     * @return
     * * The zeroToTen
     */
    fun getZeroToTen(): Double {
        return zeroToTen!!
    }

    /**

     * @param zeroToTen
     * * The zeroToTen
     */
    fun setZeroToTen(zeroToTen: Double?) {
        this.zeroToTen = zeroToTen
    }

    /**

     * @return
     * * The tenToTwenty
     */
    fun getTenToTwenty(): Double {
        return tenToTwenty!!
    }

    /**

     * @param tenToTwenty
     * * The tenToTwenty
     */
    fun setTenToTwenty(tenToTwenty: Double?) {
        this.tenToTwenty = tenToTwenty
    }

    /**

     * @return
     * * The twentyToThirty
     */
    fun getTwentyToThirty(): Double {
        return twentyToThirty!!
    }

    /**

     * @param twentyToThirty
     * * The twentyToThirty
     */
    fun setTwentyToThirty(twentyToThirty: Double?) {
        this.twentyToThirty = twentyToThirty
    }

    /**

     * @return
     * * The thirtyToEnd
     */
    fun getThirtyToEnd(): Double {
        return thirtyToEnd!!
    }

    /**

     * @param thirtyToEnd
     * * The thirtyToEnd
     */
    fun setThirtyToEnd(thirtyToEnd: Double?) {
        this.thirtyToEnd = thirtyToEnd
    }
}
