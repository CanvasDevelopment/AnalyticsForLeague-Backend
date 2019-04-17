package db.champion

class ChampData {

    /**
     * "Aatrox":{"version":"8.16.1","id":"Aatrox","key":"266","name" : "Aatrox","title":"the Darkin Blade","blurb":"Once honored defenders of Shurima against the Void, Aatrox and his brethren would eventually become an even greater threat to Runeterra, and were defeated only by cunning mortal sorcery. But after centuries of imprisonment, Aatrox was the first to find...","info":{"attack":8,"defense":4,"magic":3,"difficulty":4},"image":{"full":"Aatrox.png","sprite":"champion0.png","group":"champion","x":0,"y":0,"w":48,"h":48},"tags":["Fighter","Tank"],"partype":"Blood Well","stats":{"hp":580,"hpperlevel":80,"mp":0,"mpperlevel":0,"movespeed":345,"armor":33,"armorperlevel":3.25,"spellblock":32.1,"spellblockperlevel":1.25,"attackrange":175,"hpregen":5,"hpregenperlevel":0.25,"mpregen":0,"mpregenperlevel":0,"crit":0,"critperlevel":0,"attackdamage":60,"attackdamageperlevel":5,"attackspeedoffset":-0.04,"attackspeedperlevel":2.5}}
     */

    var version : String = ""
    var id : String = ""
    var key : String = ""
    var name : String = ""
    var title : String = ""
    var blurb : String = ""
    var summonerId : String = "-1"
    var games : Int = 0

    constructor(version :String,
                id : String,
                key : String,
                name : String,
                title : String,
                blurb : String,
                summonerId : String,
                games : Int) {
        this.version = version
        this.id = id
        this.key = key
        this.name = name
        this.title = title
        this.blurb = blurb
        this.games = games
        this.summonerId = summonerId
    }

    constructor()
}