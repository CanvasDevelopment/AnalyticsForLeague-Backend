package model

/**
 * @author Josiah Kendall
 */
class NetworkResult<T>(var data: T?,
                       val code : Int) {

    fun setResultData(data : T) {
        this.data = data
    }

    fun getResult() : T? {
        return data
    }
}