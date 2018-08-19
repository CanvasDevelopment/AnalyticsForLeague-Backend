package util

class Utils {
    fun getVillainKey(key : String) : String {
        val keyWithoutHero = key.substring(4)
        return "villan$keyWithoutHero"
    }
}