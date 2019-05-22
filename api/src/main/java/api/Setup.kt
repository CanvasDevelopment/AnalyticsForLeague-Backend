package api

import com.github.salomonbrys.kodein.instance
import com.google.api.server.spi.config.Api
import com.google.api.server.spi.config.ApiMethod
import com.google.api.server.spi.config.ApiNamespace
import com.google.api.server.spi.config.Named
import database.DbHelper
import di.KodeinManager_api

/**
 * @author Josiah Kendall
 */
@Api(name = "setup",
        version = "v1",
        namespace = ApiNamespace(ownerDomain = "com.analyticsforleague",
                ownerName = "com.analyticsforleague",
                packagePath = "")
)

class Setup {

    private val km = KodeinManager_api()
    private val dbHelper = km.kodein.instance<DbHelper>()

    @ApiMethod(name = "initiate",
            httpMethod = ApiMethod.HttpMethod.GET,
            path = "initiate/{password}")
    fun initiate(@Named("password") password : String) {
        if (!password.isEmpty()) {
            // do initiate
            dbHelper.connect()
            initiateTables()
        }
    }

    private fun initiateTables() {
        dbHelper.executeSQLScript(sql31)
        dbHelper.executeSQLScript(sql)
        dbHelper.executeSQLScript(otherSql)
        dbHelper.executeSQLScript(sql3)
        dbHelper.executeSQLScript(sql4)
        dbHelper.executeSQLScript(sql5)
        dbHelper.executeSQLScript(sql6)
        dbHelper.executeSQLScript(sql7)
        dbHelper.executeSQLScript(sql8)
        dbHelper.executeSQLScript(sql9)
        dbHelper.executeSQLScript(sql10)
        dbHelper.executeSQLScript(sql11)
        dbHelper.executeSQLScript(sql12)
        dbHelper.executeSQLScript(sql13)
        dbHelper.executeSQLScript(sql14)
        dbHelper.executeSQLScript(sql15)
        dbHelper.executeSQLScript(sql16)
        dbHelper.executeSQLScript(sql17)
        dbHelper.executeSQLScript(sql18)
        dbHelper.executeSQLScript(sql19)
        dbHelper.executeSQLScript(sql20)
        dbHelper.executeSQLScript(sql21)
        dbHelper.executeSQLScript(sql22)
        dbHelper.executeSQLScript(sql24)
        dbHelper.executeSQLScript(sql25)
        dbHelper.executeSQLScript(sql26)
        dbHelper.executeSQLScript(sql27)
        dbHelper.executeSQLScript(sql28)
        dbHelper.executeSQLScript(sql29)
        dbHelper.executeSQLScript(sql30)
    }

    private val sql31 = "create table ChampData\n" +
            "(\n" +
            "  version    varchar(32)   null,\n" +
            "  id         varchar(32)   not null\n" +
            "    primary key,\n" +
            "  champ_key  varchar(32)   null,\n" +
            "  champ_name varchar(32)   null,\n" +
            "  title      varchar(64)   null,\n" +
            "  burb       varchar(1024) null\n" +
            ");"

    // TODO make this read the string from the [initiate.sql] file
    private val sql = "Create Table Ban(\n" +
            "  Id INTEGER PRIMARY KEY AUTO_INCREMENT,\n" +
            "  ChampionId INTEGER,\n" +
            "  TeamId INTEGER,\n" +
            "  PickTurn INTEGER\n" +
            ");\n"
//            "\n" +
//            "\n" +
            private val otherSql = "Create TABLE ChampionImage(\n" +
            "  Id INTEGER PRIMARY KEY AUTO_INCREMENT,\n" +
            "  Full VARCHAR(32),\n" +
            "  Sprite VARCHAR(32),\n" +
            "  ImageGroup VARCHAR(32),\n" +
            "  X INTEGER,\n" +
            "  Y INTEGER,\n" +
            "  W INTEGER,\n" +
            "  H INTEGER\n" +
            ");\n"
            private val sql3 = "Create TABLE Champion(\n" +
            "  Id INTEGER PRIMARY KEY,\n" +
            "  Name VARCHAR(64),\n" +
            "ChampionKey VARCHAR(32),\n" +
            "Title VARCHAR(64)\n" +
            ");\n"
//            "\n" +
//            "\n" +
          private val sql4 =  "Create TABLE creepspermin(\n" +
            "  Id INTEGER PRIMARY KEY AUTO_INCREMENT,\n" +
            "  TimelineId DOUBLE,\n" +
            "  ZeroToTen DOUBLE,\n" +
            "  TenToTwenty DOUBLE,\n" +
            "  TwentyToThirty DOUBLE,\n" +
            "  ThirtyToEnd DOUBLE\n" +
            ");\n"
//            "\n" +
            private val sql5 =   "Create TABLE xppermin(\n" +
            "  Id INTEGER PRIMARY KEY AUTO_INCREMENT,\n" +
            "  TimelineId DOUBLE,\n" +
            "  ZeroToTen DOUBLE,\n" +
            "  TenToTwenty DOUBLE,\n" +
            "  TwentyToThirty DOUBLE,\n" +
            "  ThirtyToEnd DOUBLE\n" +
            ");\n"
//            "\n" +
           private val sql6 = "Create TABLE goldpermin(\n" +
            "  Id INTEGER PRIMARY KEY AUTO_INCREMENT,\n" +
            "  TimelineId DOUBLE,\n" +
            "  ZeroToTen DOUBLE,\n" +
            "  TenToTwenty DOUBLE,\n" +
            "  TwentyToThirty DOUBLE,\n" +
            "  ThirtyToEnd DOUBLE\n" +
            ");\n"
//            "\n" +
private val sql7 = "Create TABLE csdiffpermin(\n" +
            "  Id INTEGER PRIMARY KEY AUTO_INCREMENT,\n" +
            "  TimelineId DOUBLE,\n" +
            "  ZeroToTen DOUBLE,\n" +
            "  TenToTwenty DOUBLE,\n" +
            "  TwentyToThirty DOUBLE,\n" +
            "  ThirtyToEnd DOUBLE\n" +
            ");\n"
//            "\n" +
            private val sql8 = "Create TABLE xpdiffpermin(\n" +
            "  Id INTEGER PRIMARY KEY AUTO_INCREMENT,\n" +
            "  TimelineId DOUBLE,\n" +
            "  ZeroToTen DOUBLE,\n" +
            "  TenToTwenty DOUBLE,\n" +
            "  TwentyToThirty DOUBLE,\n" +
            "  ThirtyToEnd DOUBLE\n" +
            ");\n"
//            "\n" +
           private val sql9 = "Create TABLE damagetakenpermin(\n" +
            "  Id INTEGER PRIMARY KEY AUTO_INCREMENT,\n" +
            "  TimelineId DOUBLE,\n" +
            "  ZeroToTen DOUBLE,\n" +
            "  TenToTwenty DOUBLE,\n" +
            "  TwentyToThirty DOUBLE,\n" +
            "  ThirtyToEnd DOUBLE\n" +
            ");\n"
            private val sql10 = "Create TABLE damagetakendiffpermin(\n" +
            "  Id INTEGER PRIMARY KEY AUTO_INCREMENT,\n" +
            "  TimelineId DOUBLE,\n" +
            "  ZeroToTen DOUBLE,\n" +
            "  TenToTwenty DOUBLE,\n" +
            "  TwentyToThirty DOUBLE,\n" +
            "  ThirtyToEnd DOUBLE\n" +
            ");\n"
//            "\n" +
            private val sql11 = "CREATE TABLE Mastery(\n" +
            "  Id INTEGER AUTO_INCREMENT PRIMARY KEY,\n" +
            "  MasteryId INTEGER,\n" +
            "  ParticipantRowId BIGINT,\n" +
            "  _Rank INTEGER\n" +
            ");\n"
//            "\n" +
            private val sql12 = "Create TABLE MatchTable(\n" +
            "  GameId BIGINT PRIMARY KEY ,\n" +
            "  PlatformId VARCHAR(32),\n" +
            "  GameCreation BIGINT,\n" +
            "  GameDuration BIGINT,\n" +
            "  QueueId INTEGER,\n" +
            "  MapId INTEGER,\n" +
            "  SeasonId INTEGER,\n" +
            "  GameVersion VARCHAR(64),\n" +
            "  GameMode VARCHAR(32),\n" +
            "  GameType VARCHAR(32)\n"+
            ");\n"
//            "\n" +
            private val sql13 = "CREATE TABLE MatchSummary(\n" +
            "  Id BIGINT AUTO_INCREMENT PRIMARY KEY,\n" +
            "  PlatformId VARCHAR(64),\n" +
            "  GameId BIGINT,\n" +
            "  Champion INTEGER,\n" +
            "  Queue VARCHAR(32),\n" +
            "  Season VARCHAR(32),\n" +
            "  TIMESTAMP BIGINT,\n" +
            "  Role VARCHAR(16),\n" +
            "  Lane VARCHAR(16),\n" +
            "  SummonerId VarChar(64)\n" +
            ");\n"
//            "\n" +
           private val sql14 =  "Create TABLE Participant(\n" +
            "  Id BIGINT AUTO_INCREMENT PRIMARY KEY,\n" +
            "  ParticipantId INTEGER,\n" +
            "  TeamId INTEGER,\n" +
            "  ChampionId INTEGER,\n" +
            "  Spell1Id INTEGER,\n" +
            "  Spell2Id INTEGER,\n" +
            "  SummonerId VARCHAR(64),\n" +
            "  HighestSeasonAchievedTier VARCHAR(32),\n" +
            "  GameId BIGINT,\n" +
            "  Role VARCHAR(16),\n" +
            "  Lane VARCHAR(16)\n" +
            ");\n"
            private val sql15 = "CREATE TABLE ParticipantIdentity(\n" +
            "  Id INTEGER PRIMARY KEY AUTO_INCREMENT,\n" +
            "  ParticipantId INTEGER,\n" +
            "  SummonerId VARCHAR(64),\n" +
            "  GameId BIGINT,\n" +
            "  TeamId INTEGER,\n" +
            "  Role VARCHAR(16),\n" +
            "  Lane VARCHAR(16)\n" +
            ");\n"
//            "\n" +
            private val sql16 = "CREATE TABLE Player(\n" +
            "  Id INTEGER AUTO_INCREMENT PRIMARY KEY ,\n" +
            "  ParticipantIdentityRowId BIGINT,\n" +
            "  PlatformId VARCHAR(32),\n" +
            "  AccountId VARCHAR(64),\n" +
            "  SummonerName VARCHAR(64),\n" +
            "  currentPlatformId VARCHAR(32),\n" +
            "  matchHistoryUri VARCHAR(64),\n" +
            "  profileIcon INTEGER\n" +
            ");\n"
//            "\n" +
            private val sql17 ="CREATE TABLE Rune(\n" +
            "  Id INTEGER AUTO_INCREMENT PRIMARY KEY,\n" +
            "  ParticipantRowId INTEGER,\n" +
            "  RuneId INTEGER,\n" +
            "  _Rank INTEGER\n" +
            ");\n"
//            "\n" +
           private val sql18 = "Create TABLE Stats(\n" +
            "  Id INTEGER AUTO_INCREMENT PRIMARY KEY,\n" +
            "  ParticipantRowId INTEGER,\n" +
            "  ParticipantId BIGINT,\n" +
            "  Win BOOLEAN,\n" +
            "  Item0 INTEGER,\n" +
            "  Item1 INTEGER,\n" +
            "  Item2 INTEGER,\n" +
            "  Item3 INTEGER,\n" +
            "  Item4 INTEGER,\n" +
            "  Item5 INTEGER,\n" +
            "  Item6 INTEGER,\n" +
            "  Kills INTEGER,\n" +
            "  Deaths INTEGER,\n" +
            "  Assists INTEGER,\n" +
            "  LargestKillingSpree INTEGER,\n" +
            "  LargestMultiKill INTEGER,\n" +
            "  KillingSprees INTEGER,\n" +
            "  LongestTimeSpentLiving INTEGER,\n" +
            "  DoubleKills INTEGER,\n" +
            "  TripleKills INTEGER,\n" +
            "  QuadraKills INTEGER,\n" +
            "  PentaKills INTEGER,\n" +
            "  UnrealKills INTEGER,\n" +
            "  TotalDamageDealt INTEGER,\n" +
            "  MagicDamageDealt INTEGER,\n" +
            "  PhysicalDamageDealt INTEGER,\n" +
            "  TrueDamageDealt INTEGER,\n" +
            "  LargestCriticalStrike INTEGER,\n" +
            "  TotalDamageDealtTOChampions INTEGER,\n" +
            "  magicDamageDealtToChampions INTEGER,\n" +
            "  physicalDamageDealtToChampions INTEGER,\n" +
            "  trueDamageDealtToChampions INTEGER,\n" +
            "  totalHeal INTEGER,\n" +
            "  totalUnitsHealed INTEGER,\n" +
            "  damageSelfMitigated INTEGER,\n" +
            "  damageDealtToObjectives INTEGER,\n" +
            "  damageDealtToTurrets INTEGER,\n" +
            "  visionScore INTEGER,\n" +
            "  timeCCingOthers INTEGER,\n" +
            "  totalDamageTaken INTEGER,\n" +
            "  magicDamageTaken INTEGER,\n" +
            "  physicalDamageTaken INTEGER,\n" +
            "  trueDamageTaken INTEGER,\n" +
            "  goldEarned INTEGER,\n" +
            "  goldSpent INTEGER,\n" +
            "  turretKills INTEGER,\n" +
            "  inhibitorKills INTEGER,\n" +
            "  totalMinionsKilled INTEGER,\n" +
            "  neutralMinionsKilled INTEGER,\n" +
            "  neutralMinionsKilledTeamJungle INTEGER,\n" +
            "  neutralMinionsKilledEnemyJungle INTEGER,\n" +
            "  totalTimeCrowdControlDealt INTEGER,\n" +
            "  champLevel INTEGER,\n" +
            "  visionWardsBoughtInGame INTEGER,\n" +
            "  sightWardsBoughtInGame INTEGER,\n" +
            "  wardsPlaced INTEGER,\n" +
            "  wardsKilled INTEGER,\n" +
            "  firstBloodKill BOOLEAN,\n" +
            "  firstBloodAssist BOOLEAN,\n" +
            "  firstTowerKill BOOLEAN,\n" +
            "  firstTowerAssist BOOLEAN,\n" +
            "  firstInhibitorKill BOOLEAN,\n" +
            "  firstInhibitorAssist BOOLEAN,\n" +
            "  combatPlayerScore INTEGER,\n" +
            "  objectivePlayerScore INTEGER,\n" +
            "  totalPlayerScore INTEGER,\n" +
            "  totalScoreRank INTEGER\n" +
            ");\n"
//            "\n" +
            private val sql19 = "Create TABLE Team(\n" +
            "  Id INTEGER PRIMARY KEY AUTO_INCREMENT,\n" +
            "  TeamId INTEGER,\n" +
            "  Win VARCHAR(5),\n" +
            "  FirstBlood BOOLEAN,\n" +
            "  FirstTower BOOLEAN,\n" +
            "  FirstInhibitor BOOLEAN,\n" +
            "  FirstBaron BOOLEAN,\n" +
            "  FirstDragon BOOLEAN,\n" +
            "  FirstRiftHerald BOOLEAN,\n" +
            "  TowerKills INTEGER,\n" +
            "  InhibitorKills INTEGER,\n" +
            "  BaronKills INTEGER,\n" +
            "  DragonKills INTEGER,\n" +
            "  VileMawKills INTEGER,\n" +
            "  RiftHeraldKills INTEGER,\n" +
            "  DominionVictoryScore INTEGER,\n" +
            "  GameId BIGINT\n" +
            ");\n"
//            "\n" +
            private val sql20 = "CREATE TABLE Timeline(\n" +
            "  Id INTEGER AUTO_INCREMENT PRIMARY KEY,\n" +
            "  ParticipantId INTEGER,\n" +
            "  ParticipantRowId INTEGER,\n" +
            "  Role VARCHAR(16),\n" +
            "  Lane VARCHAR(16)\n" +
            ");\n"
//            "\n" +
            private val sql21 = "Create TABLE RiotApiRequests(\n" +
            "  id INTEGER PRIMARY KEY AUTO_INCREMENT,\n" +
            "  requestTime LONG\n" +
            ");\n"
//            "\n" +
            private val sql22 = "CREATE TABLE Jungle_SummaryStats(\n" +
            "  id BIGINT AUTO_INCREMENT PRIMARY KEY,\n" +
            "  gameId LONG,\n" +
            "  heroSummonerId VARCHAR(64),\n" +
            "  heroChampId INTEGER,\n" +
            "  villanChampId INTEGER,\n" +
            "  # Team stats\n" +
            "  heroTeamId INTEGER,\n" +
            "  villanTeamId INTEGER,\n" +
            "  heroWin BOOLEAN,\n" +
            "  villanWin BOOLEAN,\n" +
            "  heroTeamTowerKills INTEGER,\n" +
            "  villanTeamTowerKills INTEGER,\n" +
            "  heroTeamDragonKills INTEGER,\n" +
            "  villanTeamDragonKills INTEGER,\n" +
            "  heroTeamRiftHeraldKills INTEGER,\n" +
            "  villanTeamRiftHeraldKills INTEGER,\n" +
            "  heroTeamBaronKills INTEGER,\n" +
            "  villanTeamBaronKills INTEGER,\n" +
            "  # Player stats\n" +
            "  heroKills INTEGER,\n" +
            "  villanKills INTEGER,\n" +
            "  heroDeaths INTEGER,\n" +
            "  villanDeaths INTEGER,\n" +
            "  heroAssists INTEGER,\n" +
            "  villanAssists INTEGER,\n" +
            "  heroWardsPlaced INTEGER,\n" +
            "  villanWardsPlaced INTEGER,\n" +
            "  heroWardsKilled INTEGER,\n" +
            "  villanWardsKilled INTEGER,\n" +
            "  # Start of deltas\n" +
            "  heroGoldEarlyGame FLOAT,\n" +
            "  villanGoldEarlyGame FLOAT,\n" +
            "  heroGoldMidGame FLOAT,\n" +
            "  villanGoldMidGame FLOAT,\n" +
            "  heroGoldLateGame FLOAT,\n" +
            "  villanGoldLateGame FLOAT,\n" +
            "  heroCreepsEarlyGame FLOAT,\n" +
            "  villanCreepsEarlyGame FLOAT,\n" +
            "  heroCreepsMidGame FLOAT,\n" +
            "  villanCreepsMidGame FLOAT,\n" +
            "  heroCreepsLateGame FLOAT,\n" +
            "  villanCreepsLateGame FLOAT,\n" +
            "  heroDamageEarlyGame FLOAT,\n" +
            "  villanDamageEarlyGame FLOAT,\n" +
            "  heroDamageMidGame FLOAT,\n" +
            "  villanDamageMidGame FLOAT,\n" +
            "  heroDamageLateGame FLOAT,\n" +
            "  villanDamageLateGame FLOAT,\n" +
            "  heroXpEarlyGame FLOAT,\n" +
            "  villanXpEarlyGame FLOAT,\n" +
            "  heroXpMidGame FLOAT,\n" +
            "  villanXpMidGame FLOAT,\n" +
            "  heroXpLateGame FLOAT,\n" +
            "  villanXpLateGame FLOAT\n" +
            ");\n"
//            "\n" +
            private val sql24 = "CREATE TABLE Top_SummaryStats(\n" +
            "  id BIGINT AUTO_INCREMENT PRIMARY KEY,\n" +
            "  gameId LONG,\n" +
            "  heroSummonerId VARCHAR(64),\n" +
            "  heroChampId INTEGER,\n" +
            "  villanChampId INTEGER,\n" +
            "  # Team stats\n" +
            "  heroTeamId INTEGER,\n" +
            "  villanTeamId INTEGER,\n" +
            "  heroWin BOOLEAN,\n" +
            "  villanWin BOOLEAN,\n" +
            "  heroTeamTowerKills INTEGER,\n" +
            "  villanTeamTowerKills INTEGER,\n" +
            "  heroTeamDragonKills INTEGER,\n" +
            "  villanTeamDragonKills INTEGER,\n" +
            "  heroTeamRiftHeraldKills INTEGER,\n" +
            "  villanTeamRiftHeraldKills INTEGER,\n" +
            "  heroTeamBaronKills INTEGER,\n" +
            "  villanTeamBaronKills INTEGER,\n" +
            "  # Player stats\n" +
            "  heroKills INTEGER,\n" +
            "  villanKills INTEGER,\n" +
            "  heroDeaths INTEGER,\n" +
            "  villanDeaths INTEGER,\n" +
            "  heroAssists INTEGER,\n" +
            "  villanAssists INTEGER,\n" +
            "  heroWardsPlaced INTEGER,\n" +
            "  villanWardsPlaced INTEGER,\n" +
            "  heroWardsKilled INTEGER,\n" +
            "  villanWardsKilled INTEGER,\n" +
            "  # Start of deltas\n" +
            "  heroGoldEarlyGame FLOAT,\n" +
            "  villanGoldEarlyGame FLOAT,\n" +
            "  heroGoldMidGame FLOAT,\n" +
            "  villanGoldMidGame FLOAT,\n" +
            "  heroGoldLateGame FLOAT,\n" +
            "  villanGoldLateGame FLOAT,\n" +
            "  heroCreepsEarlyGame FLOAT,\n" +
            "  villanCreepsEarlyGame FLOAT,\n" +
            "  heroCreepsMidGame FLOAT,\n" +
            "  villanCreepsMidGame FLOAT,\n" +
            "  heroCreepsLateGame FLOAT,\n" +
            "  villanCreepsLateGame FLOAT,\n" +
            "  heroDamageEarlyGame FLOAT,\n" +
            "  villanDamageEarlyGame FLOAT,\n" +
            "  heroDamageMidGame FLOAT,\n" +
            "  villanDamageMidGame FLOAT,\n" +
            "  heroDamageLateGame FLOAT,\n" +
            "  villanDamageLateGame FLOAT,\n" +
            "  heroXpEarlyGame FLOAT,\n" +
            "  villanXpEarlyGame FLOAT,\n" +
            "  heroXpMidGame FLOAT,\n" +
            "  villanXpMidGame FLOAT,\n" +
            "  heroXpLateGame FLOAT,\n" +
            "  villanXpLateGame FLOAT\n" +
            ");\n"
//            "\n" +
            private val sql25 = "CREATE TABLE Mid_SummaryStats(\n" +
            "  id BIGINT AUTO_INCREMENT PRIMARY KEY,\n" +
            "  gameId LONG,\n" +
            "  heroSummonerId VARCHAR(64),\n" +
            "  heroChampId INTEGER,\n" +
            "  villanChampId INTEGER,\n" +
            "  # Team stats\n" +
            "  heroTeamId INTEGER,\n" +
            "  villanTeamId INTEGER,\n" +
            "  heroWin BOOLEAN,\n" +
            "  villanWin BOOLEAN,\n" +
            "  heroTeamTowerKills INTEGER,\n" +
            "  villanTeamTowerKills INTEGER,\n" +
            "  heroTeamDragonKills INTEGER,\n" +
            "  villanTeamDragonKills INTEGER,\n" +
            "  heroTeamRiftHeraldKills INTEGER,\n" +
            "  villanTeamRiftHeraldKills INTEGER,\n" +
            "  heroTeamBaronKills INTEGER,\n" +
            "  villanTeamBaronKills INTEGER,\n" +
            "  # Player stats\n" +
            "  heroKills INTEGER,\n" +
            "  villanKills INTEGER,\n" +
            "  heroDeaths INTEGER,\n" +
            "  villanDeaths INTEGER,\n" +
            "  heroAssists INTEGER,\n" +
            "  villanAssists INTEGER,\n" +
            "  heroWardsPlaced INTEGER,\n" +
            "  villanWardsPlaced INTEGER,\n" +
            "  heroWardsKilled INTEGER,\n" +
            "  villanWardsKilled INTEGER,\n" +
            "  # Start of deltas\n" +
            "  heroGoldEarlyGame FLOAT,\n" +
            "  villanGoldEarlyGame FLOAT,\n" +
            "  heroGoldMidGame FLOAT,\n" +
            "  villanGoldMidGame FLOAT,\n" +
            "  heroGoldLateGame FLOAT,\n" +
            "  villanGoldLateGame FLOAT,\n" +
            "  heroCreepsEarlyGame FLOAT,\n" +
            "  villanCreepsEarlyGame FLOAT,\n" +
            "  heroCreepsMidGame FLOAT,\n" +
            "  villanCreepsMidGame FLOAT,\n" +
            "  heroCreepsLateGame FLOAT,\n" +
            "  villanCreepsLateGame FLOAT,\n" +
            "  heroDamageEarlyGame FLOAT,\n" +
            "  villanDamageEarlyGame FLOAT,\n" +
            "  heroDamageMidGame FLOAT,\n" +
            "  villanDamageMidGame FLOAT,\n" +
            "  heroDamageLateGame FLOAT,\n" +
            "  villanDamageLateGame FLOAT,\n" +
            "  heroXpEarlyGame FLOAT,\n" +
            "  villanXpEarlyGame FLOAT,\n" +
            "  heroXpMidGame FLOAT,\n" +
            "  villanXpMidGame FLOAT,\n" +
            "  heroXpLateGame FLOAT,\n" +
            "  villanXpLateGame FLOAT\n" +
            ");\n"
           private val sql26 = "CREATE TABLE Support_SummaryStats(\n" +
            "  id BIGINT AUTO_INCREMENT PRIMARY KEY,\n" +
            "  gameId LONG,\n" +
            "  heroSummonerId VARCHAR(64),\n" +
            "  heroChampId INTEGER,\n" +
            "  villanChampId INTEGER,\n" +
            "  # Team stats\n" +
            "  heroTeamId INTEGER,\n" +
            "  villanTeamId INTEGER,\n" +
            "  heroWin BOOLEAN,\n" +
            "  villanWin BOOLEAN,\n" +
            "  heroTeamTowerKills INTEGER,\n" +
            "  villanTeamTowerKills INTEGER,\n" +
            "  heroTeamDragonKills INTEGER,\n" +
            "  villanTeamDragonKills INTEGER,\n" +
            "  heroTeamRiftHeraldKills INTEGER,\n" +
            "  villanTeamRiftHeraldKills INTEGER,\n" +
            "  heroTeamBaronKills INTEGER,\n" +
            "  villanTeamBaronKills INTEGER,\n" +
            "  # Player stats\n" +
            "  heroKills INTEGER,\n" +
            "  villanKills INTEGER,\n" +
            "  heroDeaths INTEGER,\n" +
            "  villanDeaths INTEGER,\n" +
            "  heroAssists INTEGER,\n" +
            "  villanAssists INTEGER,\n" +
            "  heroWardsPlaced INTEGER,\n" +
            "  villanWardsPlaced INTEGER,\n" +
            "  heroWardsKilled INTEGER,\n" +
            "  villanWardsKilled INTEGER,\n" +
            "  # Start of deltas\n" +
            "  heroGoldEarlyGame FLOAT,\n" +
            "  villanGoldEarlyGame FLOAT,\n" +
            "  heroGoldMidGame FLOAT,\n" +
            "  villanGoldMidGame FLOAT,\n" +
            "  heroGoldLateGame FLOAT,\n" +
            "  villanGoldLateGame FLOAT,\n" +
            "  heroCreepsEarlyGame FLOAT,\n" +
            "  villanCreepsEarlyGame FLOAT,\n" +
            "  heroCreepsMidGame FLOAT,\n" +
            "  villanCreepsMidGame FLOAT,\n" +
            "  heroCreepsLateGame FLOAT,\n" +
            "  villanCreepsLateGame FLOAT,\n" +
            "  heroDamageEarlyGame FLOAT,\n" +
            "  villanDamageEarlyGame FLOAT,\n" +
            "  heroDamageMidGame FLOAT,\n" +
            "  villanDamageMidGame FLOAT,\n" +
            "  heroDamageLateGame FLOAT,\n" +
            "  villanDamageLateGame FLOAT,\n" +
            "  heroXpEarlyGame FLOAT,\n" +
            "  villanXpEarlyGame FLOAT,\n" +
            "  heroXpMidGame FLOAT,\n" +
            "  villanXpMidGame FLOAT,\n" +
            "  heroXpLateGame FLOAT,\n" +
            "  villanXpLateGame FLOAT\n" +
            ");\n"
//            "\n" +
            private val sql27 = "CREATE TABLE ADC_SummaryStats(\n" +
            "  id BIGINT AUTO_INCREMENT PRIMARY KEY,\n" +
            "  gameId LONG,\n" +
            "  heroSummonerId VARCHAR(64),\n" +
            "  heroChampId INTEGER,\n" +
            "  villanChampId INTEGER,\n" +
            "  # Team stats\n" +
            "  heroTeamId INTEGER,\n" +
            "  villanTeamId INTEGER,\n" +
            "  heroWin BOOLEAN,\n" +
            "  villanWin BOOLEAN,\n" +
            "  heroTeamTowerKills INTEGER,\n" +
            "  villanTeamTowerKills INTEGER,\n" +
            "  heroTeamDragonKills INTEGER,\n" +
            "  villanTeamDragonKills INTEGER,\n" +
            "  heroTeamRiftHeraldKills INTEGER,\n" +
            "  villanTeamRiftHeraldKills INTEGER,\n" +
            "  heroTeamBaronKills INTEGER,\n" +
            "  villanTeamBaronKills INTEGER,\n" +
            "  # Player stats\n" +
            "  heroKills INTEGER,\n" +
            "  villanKills INTEGER,\n" +
            "  heroDeaths INTEGER,\n" +
            "  villanDeaths INTEGER,\n" +
            "  heroAssists INTEGER,\n" +
            "  villanAssists INTEGER,\n" +
            "  heroWardsPlaced INTEGER,\n" +
            "  villanWardsPlaced INTEGER,\n" +
            "  heroWardsKilled INTEGER,\n" +
            "  villanWardsKilled INTEGER,\n" +
            "  # Start of deltas\n" +
            "  heroGoldEarlyGame FLOAT,\n" +
            "  villanGoldEarlyGame FLOAT,\n" +
            "  heroGoldMidGame FLOAT,\n" +
            "  villanGoldMidGame FLOAT,\n" +
            "  heroGoldLateGame FLOAT,\n" +
            "  villanGoldLateGame FLOAT,\n" +
            "  heroCreepsEarlyGame FLOAT,\n" +
            "  villanCreepsEarlyGame FLOAT,\n" +
            "  heroCreepsMidGame FLOAT,\n" +
            "  villanCreepsMidGame FLOAT,\n" +
            "  heroCreepsLateGame FLOAT,\n" +
            "  villanCreepsLateGame FLOAT,\n" +
            "  heroDamageEarlyGame FLOAT,\n" +
            "  villanDamageEarlyGame FLOAT,\n" +
            "  heroDamageMidGame FLOAT,\n" +
            "  villanDamageMidGame FLOAT,\n" +
            "  heroDamageLateGame FLOAT,\n" +
            "  villanDamageLateGame FLOAT,\n" +
            "  heroXpEarlyGame FLOAT,\n" +
            "  villanXpEarlyGame FLOAT,\n" +
            "  heroXpMidGame FLOAT,\n" +
            "  villanXpMidGame FLOAT,\n" +
            "  heroXpLateGame FLOAT,\n" +
            "  villanXpLateGame FLOAT\n" +
            ");\n"
//            "\n" +
            private val sql28 = "Create TABLE Summoner(\n" +
            "  Id VARCHAR(64) PRIMARY KEY,\n" +
            "  AccountId VARCHAR(64),\n" +
            "  SummonerName VARCHAR(64),\n" +
            "  ProfileIconId INTEGER,\n" +
            "  SummonerLevel INTEGER,\n" +
            "  RevisionDate BIGINT\n" +
            ");\n"
//            "\n" +
            private val sql29 ="CREATE TABLE RiotApiEndpointRateLimitStatus(\n" +
            "  Id INTEGER PRIMARY KEY,\n" +
            "  retryAfter INTEGER\n" +
            ");\n"
//            "\n" +
            private val sql30 = "Create Table RateLimitBucket(\n" +
            "  Id INTEGER PRIMARY KEY AUTO_INCREMENT,\n" +
            "  EndPointId INTEGER,\n" +
            "  MaxRequests INTEGER,\n" +
            "  RequestCount INTEGER,\n" +
            "  FirstRequestTime BIGINT,\n" +
            "  RateDuration INTEGER\n" +
            ");"


}