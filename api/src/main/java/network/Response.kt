package network

/**
 * @author Josiah Kendall
 */
class Response() {

    var resultCode : Int = 404
    var data : String = ""

    constructor(resultCode : Int,
                data : String) : this() {
        this.resultCode = resultCode
        this.data = data
    }
}