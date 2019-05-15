package api.champ

class ChampWrapper {
    /**
     * "type":"champion","format":"standAloneComplex","version":"8.16.1","data":
     */
    var type : String = "";
    var format : String = ""
    var version :String = ""
    var data : Map<String, ChampData>? = null

    constructor(type : String,
                format : String,
                version : String,
                data : Map<String, ChampData>) {
        this.type = type
        this.format = format
        this.version = version
        this.data = data
    }

    constructor()

}