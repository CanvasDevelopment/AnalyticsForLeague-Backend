package model

/**
 * @author Josiah Kendall
 */
class Response<T>(val resultCode : Int,
               val data : T)