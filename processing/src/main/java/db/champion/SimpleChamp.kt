package db.champion

class SimpleChamp {
    var version : String = ""
    var id : String = ""
    var key : String = ""
    var name : String = ""
    var title : String = ""
    var blurb : String = ""

    constructor(version :String,
                id : String,
                key : String,
                name : String,
                title : String,
                blurb : String) {
        this.version = version
        this.id = id
        this.key = key
        this.name = name
        this.title = title
        this.blurb = blurb
    }

    constructor()
}