Create Table Ban(
  Id INTEGER PRIMARY KEY AUTO_INCREMENT,
  ChampionId INTEGER,
  TeamId INTEGER,
  PickTurn INTEGER
);

Create TABLE Champion(
  Id INTEGER PRIMARY KEY,
  ChampionKey VarChar(32),
  Name VARCHAR(64),
  Title VARCHAR(64)
);

Create TABLE ChampionImage(
  Id INTEGER PRIMARY KEY AUTO_INCREMENT,
  Full VARCHAR(32),
  Sprite VARCHAR(32),
  ImageGroup VARCHAR(32),
  X INTEGER,
  Y INTEGER,
  W INTEGER,
  H INTEGER
);


Create TABLE creepspermin(
  Id INTEGER PRIMARY KEY AUTO_INCREMENT,
  TimelineId DOUBLE,
  ZeroToTen DOUBLE,
  TenToTwenty DOUBLE,
  TwentyToThirty DOUBLE,
  ThirtyToEnd DOUBLE
);

Create TABLE xppermin(
  Id INTEGER PRIMARY KEY AUTO_INCREMENT,
  TimelineId DOUBLE,
  ZeroToTen DOUBLE,
  TenToTwenty DOUBLE,
  TwentyToThirty DOUBLE,
  ThirtyToEnd DOUBLE
);

Create TABLE goldpermin(
  Id INTEGER PRIMARY KEY AUTO_INCREMENT,
  TimelineId DOUBLE,
  ZeroToTen DOUBLE,
  TenToTwenty DOUBLE,
  TwentyToThirty DOUBLE,
  ThirtyToEnd DOUBLE
);

Create TABLE csdiffpermin(
  Id INTEGER PRIMARY KEY AUTO_INCREMENT,
  TimelineId DOUBLE,
  ZeroToTen DOUBLE,
  TenToTwenty DOUBLE,
  TwentyToThirty DOUBLE,
  ThirtyToEnd DOUBLE
);

Create TABLE xpdiffpermin(
  Id INTEGER PRIMARY KEY AUTO_INCREMENT,
  TimelineId DOUBLE,
  ZeroToTen DOUBLE,
  TenToTwenty DOUBLE,
  TwentyToThirty DOUBLE,
  ThirtyToEnd DOUBLE
);

Create TABLE damagetakenpermin(
  Id INTEGER PRIMARY KEY AUTO_INCREMENT,
  TimelineId DOUBLE,
  ZeroToTen DOUBLE,
  TenToTwenty DOUBLE,
  TwentyToThirty DOUBLE,
  ThirtyToEnd DOUBLE
);
Create TABLE damagetakendiffpermin(
  Id INTEGER PRIMARY KEY AUTO_INCREMENT,
  TimelineId DOUBLE,
  ZeroToTen DOUBLE,
  TenToTwenty DOUBLE,
  TwentyToThirty DOUBLE,
  ThirtyToEnd DOUBLE
);

CREATE TABLE Mastery(
  Id INTEGER AUTO_INCREMENT PRIMARY KEY,
  MasteryId INTEGER,
  ParticipantRowId BIGINT,
  _Rank INTEGER
);

Create TABLE MatchTable(
  GameId BIGINT PRIMARY KEY ,
  PlatformId VARCHAR(32),
  GameCreation BIGINT,
  GameDuration BIGINT,
  QueueId INTEGER,
  MapId INTEGER,
  SeasonId INTEGER,
  GameVersion VARCHAR(64),
  GameMode VARCHAR(32),
  GameType VARCHAR(32)
);

CREATE TABLE MatchSummary(
  Id BIGINT AUTO_INCREMENT PRIMARY KEY,
  PlatformId VARCHAR(64),
  GameId BIGINT,
  Champion INTEGER,
  Queue VARCHAR(32),
  Season VARCHAR(32),
  TIMESTAMP BIGINT,
  Role VARCHAR(16),
  Lane VARCHAR(16),
  SummonerId BIGINT
);

Create TABLE Participant(
  Id BIGINT AUTO_INCREMENT PRIMARY KEY,
  ParticipantId INTEGER,
  TeamId INTEGER,
  ChampionId INTEGER,
  Spell1Id INTEGER,
  Spell2Id INTEGER,
  SummonerId BIGINT,
  HighestSeasonAchievedTier VARCHAR(32),
  GameId BIGINT,
  Role VARCHAR(16),
  Lane VARCHAR(16)
);
CREATE TABLE ParticipantIdentity(
  Id INTEGER PRIMARY KEY AUTO_INCREMENT,
  ParticipantId INTEGER,
  SummonerId BIGINT,
  GameId BIGINT,
  TeamId INTEGER,
  Role VARCHAR(16),
  Lane VARCHAR(16)
);

CREATE TABLE Player(
  Id INTEGER AUTO_INCREMENT PRIMARY KEY ,
  ParticipantIdentityRowId BIGINT,
  PlatformId VARCHAR(32),
  AccountId BIGINT,
  SummonerName VARCHAR(64),
  currentPlatformId VARCHAR(32),
  matchHistoryUri VARCHAR(64),
  profileIcon INTEGER
);

CREATE TABLE Rune(
  Id INTEGER AUTO_INCREMENT PRIMARY KEY,
  ParticipantRowId INTEGER,
  RuneId INTEGER,
  _Rank INTEGER
);

Create TABLE Stats(
  Id INTEGER AUTO_INCREMENT PRIMARY KEY,
  ParticipantRowId INTEGER,
  ParticipantId BIGINT,
  Win BOOLEAN,
  Item0 INTEGER,
  Item1 INTEGER,
  Item2 INTEGER,
  Item3 INTEGER,
  Item4 INTEGER,
  Item5 INTEGER,
  Item6 INTEGER,
  Kills INTEGER,
  Deaths INTEGER,
  Assists INTEGER,
  LargestKillingSpree INTEGER,
  LargestMultiKill INTEGER,
  KillingSprees INTEGER,
  LongestTimeSpentLiving INTEGER,
  DoubleKills INTEGER,
  TripleKills INTEGER,
  QuadraKills INTEGER,
  PentaKills INTEGER,
  UnrealKills INTEGER,
  TotalDamageDealt INTEGER,
  MagicDamageDealt INTEGER,
  PhysicalDamageDealt INTEGER,
  TrueDamageDealt INTEGER,
  LargestCriticalStrike INTEGER,
  TotalDamageDealtTOChampions INTEGER,
  magicDamageDealtToChampions INTEGER,
  physicalDamageDealtToChampions INTEGER,
  trueDamageDealtToChampions INTEGER,
  totalHeal INTEGER,
  totalUnitsHealed INTEGER,
  damageSelfMitigated INTEGER,
  damageDealtToObjectives INTEGER,
  damageDealtToTurrets INTEGER,
  visionScore INTEGER,
  timeCCingOthers INTEGER,
  totalDamageTaken INTEGER,
  magicDamageTaken INTEGER,
  physicalDamageTaken INTEGER,
  trueDamageTaken INTEGER,
  goldEarned INTEGER,
  goldSpent INTEGER,
  turretKills INTEGER,
  inhibitorKills INTEGER,
  totalMinionsKilled INTEGER,
  neutralMinionsKilled INTEGER,
  neutralMinionsKilledTeamJungle INTEGER,
  neutralMinionsKilledEnemyJungle INTEGER,
  totalTimeCrowdControlDealt INTEGER,
  champLevel INTEGER,
  visionWardsBoughtInGame INTEGER,
  sightWardsBoughtInGame INTEGER,
  wardsPlaced INTEGER,
  wardsKilled INTEGER,
  firstBloodKill BOOLEAN,
  firstBloodAssist BOOLEAN,
  firstTowerKill BOOLEAN,
  firstTowerAssist BOOLEAN,
  firstInhibitorKill BOOLEAN,
  firstInhibitorAssist BOOLEAN,
  combatPlayerScore INTEGER,
  objectivePlayerScore INTEGER,
  totalPlayerScore INTEGER,
  totalScoreRank INTEGER
);

Create TABLE Team(
  Id INTEGER PRIMARY KEY AUTO_INCREMENT,
  TeamId INTEGER,
  Win VARCHAR(5),
  FirstBlood BOOLEAN,
  FirstTower BOOLEAN,
  FirstInhibitor BOOLEAN,
  FirstBaron BOOLEAN,
  FirstDragon BOOLEAN,
  FirstRiftHerald BOOLEAN,
  TowerKills INTEGER,
  InhibitorKills INTEGER,
  BaronKills INTEGER,
  DragonKills INTEGER,
  VileMawKills INTEGER,
  RiftHeraldKills INTEGER,
  DominionVictoryScore INTEGER,
  GameId BIGINT
);

CREATE TABLE Timeline(
  Id INTEGER AUTO_INCREMENT PRIMARY KEY,
  ParticipantId INTEGER,
  ParticipantRowId INTEGER,
  Role VARCHAR(16),
  Lane VARCHAR(16)
);

Create TABLE RiotApiRequests(
  id INTEGER PRIMARY KEY AUTO_INCREMENT,
  requestTime LONG
);

CREATE TABLE Jungle_SummaryStats(
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  gameId LONG,
  heroSummonerId LONG,
  heroChampId INTEGER,
  villanChampId INTEGER,
  # Team stats
  heroTeamId INTEGER,
  villanTeamId INTEGER,
  heroWin BOOLEAN,
  villanWin BOOLEAN,
  heroTeamTowerKills INTEGER,
  villanTeamTowerKills INTEGER,
  heroTeamDragonKills INTEGER,
  villanTeamDragonKills INTEGER,
  heroTeamRiftHeraldKills INTEGER,
  villanTeamRiftHeraldKills INTEGER,
  heroTeamBaronKills INTEGER,
  villanTeamBaronKills INTEGER,
  # Player stats
  heroKills INTEGER,
  villanKills INTEGER,
  heroDeaths INTEGER,
  villanDeaths INTEGER,
  heroAssists INTEGER,
  villanAssists INTEGER,
  heroWardsPlaced INTEGER,
  villanWardsPlaced INTEGER,
  heroWardsKilled INTEGER,
  villanWardsKilled INTEGER,
  # Start of deltas
  heroGoldEarlyGame FLOAT,
  villanGoldEarlyGame FLOAT,
  heroGoldMidGame FLOAT,
  villanGoldMidGame FLOAT,
  heroGoldLateGame FLOAT,
  villanGoldLateGame FLOAT,
  heroCreepsEarlyGame FLOAT,
  villanCreepsEarlyGame FLOAT,
  heroCreepsMidGame FLOAT,
  villanCreepsMidGame FLOAT,
  heroCreepsLateGame FLOAT,
  villanCreepsLateGame FLOAT,
  heroDamageEarlyGame FLOAT,
  villanDamageEarlyGame FLOAT,
  heroDamageMidGame FLOAT,
  villanDamageMidGame FLOAT,
  heroDamageLateGame FLOAT,
  villanDamageLateGame FLOAT,
  heroXpEarlyGame FLOAT,
  villanXpEarlyGame FLOAT,
  heroXpMidGame FLOAT,
  villanXpMidGame FLOAT,
  heroXpLateGame FLOAT,
  villanXpLateGame FLOAT
);

CREATE TABLE Top_SummaryStats(
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  gameId LONG,
  heroSummonerId LONG,
  heroChampId INTEGER,
  villanChampId INTEGER,
  # Team stats
  heroTeamId INTEGER,
  villanTeamId INTEGER,
  heroWin BOOLEAN,
  villanWin BOOLEAN,
  heroTeamTowerKills INTEGER,
  villanTeamTowerKills INTEGER,
  heroTeamDragonKills INTEGER,
  villanTeamDragonKills INTEGER,
  heroTeamRiftHeraldKills INTEGER,
  villanTeamRiftHeraldKills INTEGER,
  heroTeamBaronKills INTEGER,
  villanTeamBaronKills INTEGER,
  # Player stats
  heroKills INTEGER,
  villanKills INTEGER,
  heroDeaths INTEGER,
  villanDeaths INTEGER,
  heroAssists INTEGER,
  villanAssists INTEGER,
  heroWardsPlaced INTEGER,
  villanWardsPlaced INTEGER,
  heroWardsKilled INTEGER,
  villanWardsKilled INTEGER,
  # Start of deltas
  heroGoldEarlyGame FLOAT,
  villanGoldEarlyGame FLOAT,
  heroGoldMidGame FLOAT,
  villanGoldMidGame FLOAT,
  heroGoldLateGame FLOAT,
  villanGoldLateGame FLOAT,
  heroCreepsEarlyGame FLOAT,
  villanCreepsEarlyGame FLOAT,
  heroCreepsMidGame FLOAT,
  villanCreepsMidGame FLOAT,
  heroCreepsLateGame FLOAT,
  villanCreepsLateGame FLOAT,
  heroDamageEarlyGame FLOAT,
  villanDamageEarlyGame FLOAT,
  heroDamageMidGame FLOAT,
  villanDamageMidGame FLOAT,
  heroDamageLateGame FLOAT,
  villanDamageLateGame FLOAT,
  heroXpEarlyGame FLOAT,
  villanXpEarlyGame FLOAT,
  heroXpMidGame FLOAT,
  villanXpMidGame FLOAT,
  heroXpLateGame FLOAT,
  villanXpLateGame FLOAT
);

CREATE TABLE Mid_SummaryStats(
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  gameId LONG,
  heroSummonerId LONG,
  heroChampId INTEGER,
  villanChampId INTEGER,
  # Team stats
  heroTeamId INTEGER,
  villanTeamId INTEGER,
  heroWin BOOLEAN,
  villanWin BOOLEAN,
  heroTeamTowerKills INTEGER,
  villanTeamTowerKills INTEGER,
  heroTeamDragonKills INTEGER,
  villanTeamDragonKills INTEGER,
  heroTeamRiftHeraldKills INTEGER,
  villanTeamRiftHeraldKills INTEGER,
  heroTeamBaronKills INTEGER,
  villanTeamBaronKills INTEGER,
  # Player stats
  heroKills INTEGER,
  villanKills INTEGER,
  heroDeaths INTEGER,
  villanDeaths INTEGER,
  heroAssists INTEGER,
  villanAssists INTEGER,
  heroWardsPlaced INTEGER,
  villanWardsPlaced INTEGER,
  heroWardsKilled INTEGER,
  villanWardsKilled INTEGER,
  # Start of deltas
  heroGoldEarlyGame FLOAT,
  villanGoldEarlyGame FLOAT,
  heroGoldMidGame FLOAT,
  villanGoldMidGame FLOAT,
  heroGoldLateGame FLOAT,
  villanGoldLateGame FLOAT,
  heroCreepsEarlyGame FLOAT,
  villanCreepsEarlyGame FLOAT,
  heroCreepsMidGame FLOAT,
  villanCreepsMidGame FLOAT,
  heroCreepsLateGame FLOAT,
  villanCreepsLateGame FLOAT,
  heroDamageEarlyGame FLOAT,
  villanDamageEarlyGame FLOAT,
  heroDamageMidGame FLOAT,
  villanDamageMidGame FLOAT,
  heroDamageLateGame FLOAT,
  villanDamageLateGame FLOAT,
  heroXpEarlyGame FLOAT,
  villanXpEarlyGame FLOAT,
  heroXpMidGame FLOAT,
  villanXpMidGame FLOAT,
  heroXpLateGame FLOAT,
  villanXpLateGame FLOAT
);
CREATE TABLE Support_SummaryStats(
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  gameId LONG,
  heroSummonerId LONG,
  heroChampId INTEGER,
  villanChampId INTEGER,
  # Team stats
  heroTeamId INTEGER,
  villanTeamId INTEGER,
  heroWin BOOLEAN,
  villanWin BOOLEAN,
  heroTeamTowerKills INTEGER,
  villanTeamTowerKills INTEGER,
  heroTeamDragonKills INTEGER,
  villanTeamDragonKills INTEGER,
  heroTeamRiftHeraldKills INTEGER,
  villanTeamRiftHeraldKills INTEGER,
  heroTeamBaronKills INTEGER,
  villanTeamBaronKills INTEGER,
  # Player stats
  heroKills INTEGER,
  villanKills INTEGER,
  heroDeaths INTEGER,
  villanDeaths INTEGER,
  heroAssists INTEGER,
  villanAssists INTEGER,
  heroWardsPlaced INTEGER,
  villanWardsPlaced INTEGER,
  heroWardsKilled INTEGER,
  villanWardsKilled INTEGER,
  # Start of deltas
  heroGoldEarlyGame FLOAT,
  villanGoldEarlyGame FLOAT,
  heroGoldMidGame FLOAT,
  villanGoldMidGame FLOAT,
  heroGoldLateGame FLOAT,
  villanGoldLateGame FLOAT,
  heroCreepsEarlyGame FLOAT,
  villanCreepsEarlyGame FLOAT,
  heroCreepsMidGame FLOAT,
  villanCreepsMidGame FLOAT,
  heroCreepsLateGame FLOAT,
  villanCreepsLateGame FLOAT,
  heroDamageEarlyGame FLOAT,
  villanDamageEarlyGame FLOAT,
  heroDamageMidGame FLOAT,
  villanDamageMidGame FLOAT,
  heroDamageLateGame FLOAT,
  villanDamageLateGame FLOAT,
  heroXpEarlyGame FLOAT,
  villanXpEarlyGame FLOAT,
  heroXpMidGame FLOAT,
  villanXpMidGame FLOAT,
  heroXpLateGame FLOAT,
  villanXpLateGame FLOAT
);

CREATE TABLE ADC_SummaryStats(
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  gameId LONG,
  heroSummonerId LONG,
  heroChampId INTEGER,
  villanChampId INTEGER,
  # Team stats
  heroTeamId INTEGER,
  villanTeamId INTEGER,
  heroWin BOOLEAN,
  villanWin BOOLEAN,
  heroTeamTowerKills INTEGER,
  villanTeamTowerKills INTEGER,
  heroTeamDragonKills INTEGER,
  villanTeamDragonKills INTEGER,
  heroTeamRiftHeraldKills INTEGER,
  villanTeamRiftHeraldKills INTEGER,
  heroTeamBaronKills INTEGER,
  villanTeamBaronKills INTEGER,
  # Player stats
  heroKills INTEGER,
  villanKills INTEGER,
  heroDeaths INTEGER,
  villanDeaths INTEGER,
  heroAssists INTEGER,
  villanAssists INTEGER,
  heroWardsPlaced INTEGER,
  villanWardsPlaced INTEGER,
  heroWardsKilled INTEGER,
  villanWardsKilled INTEGER,
  # Start of deltas
  heroGoldEarlyGame FLOAT,
  villanGoldEarlyGame FLOAT,
  heroGoldMidGame FLOAT,
  villanGoldMidGame FLOAT,
  heroGoldLateGame FLOAT,
  villanGoldLateGame FLOAT,
  heroCreepsEarlyGame FLOAT,
  villanCreepsEarlyGame FLOAT,
  heroCreepsMidGame FLOAT,
  villanCreepsMidGame FLOAT,
  heroCreepsLateGame FLOAT,
  villanCreepsLateGame FLOAT,
  heroDamageEarlyGame FLOAT,
  villanDamageEarlyGame FLOAT,
  heroDamageMidGame FLOAT,
  villanDamageMidGame FLOAT,
  heroDamageLateGame FLOAT,
  villanDamageLateGame FLOAT,
  heroXpEarlyGame FLOAT,
  villanXpEarlyGame FLOAT,
  heroXpMidGame FLOAT,
  villanXpMidGame FLOAT,
  heroXpLateGame FLOAT,
  villanXpLateGame FLOAT
);

Create TABLE Summoner(
  Id BIGINT PRIMARY KEY,
  AccountId BIGINT,
  SummonerName VARCHAR(64),
  ProfileIconId INTEGER,
  SummonerLevel INTEGER,
  RevisionDate BIGINT
);

CREATE TABLE RiotApiEndpointRateLimitStatus(
  Id INTEGER PRIMARY KEY,
  retryAfter INTEGER
);

Create Table RateLimitBucket(
  Id INTEGER PRIMARY KEY AUTO_INCREMENT,
  EndPointId INTEGER,
  MaxRequests INTEGER,
  RequestCount INTEGER,
  FirstRequestTime BIGINT,
  RateDuration INTEGER
);