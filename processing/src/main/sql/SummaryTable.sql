CREATE TABLE GameSummaryStats(
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

# ==========================================================
#  insert / update statements
# ============================================================

# Insert hero summary stats
INSERT INTO gamesummarystats (gameId,
                              heroSummonerId,
                              heroChampId,
                              heroTeamId,
                              heroWin,
                              heroTeamTowerKills,
                              heroTeamDragonKills,
                              heroTeamRiftHeraldKills,
                              heroTeamBaronKills) VALUES ();

# Insert villan summary stats using update
update gamesummarystats
SET villanChampId = 1,
  villanTeamId =1,
  villanWin = true,
  villanTeamTowerKills = 1,
  villanTeamDragonKills = 1,
  villanTeamRiftHeraldKills = 1,
  villanTeamBaronKills = 1
WHERE heroSummonerId = -1 AND gameId = -2;

# insert deltas hero
UPDATE gamesummarystats
SET heroXpEarlyGame = 1,
 heroXpMidGame = 1,
 heroXpLateGame = 1,
 heroCreepsEarlyGame = 1,
 heroCreepsMidGame = 1,
 heroCreepsLateGame = 1,
 heroDamageEarlyGame = 1,
 heroDamageMidGame = 1,
 heroDamageLateGame = 1,
 heroGoldEarlyGame = 1,
 heroGoldMidGame = 1,
 heroGoldLateGame = 1
where gameId = -1 and heroSummonerId = -1;

# Insert deltas villan
UPDATE gamesummarystats
SET villanXpEarlyGame = 1,
 villanXpMidGame = 1,
 villanXpLateGame = 1,
 villanCreepsEarlyGame = 1,
 villanCreepsMidGame = 1,
 villanCreepsLateGame = 1,
 villanDamageEarlyGame = 1,
villanDamageMidGame = 1,
 villanDamageLateGame = 1,
 villanGoldEarlyGame = 1,
 villanGoldMidGame = 1,
 villanGoldLateGame = 1
where gameId = -1 and heroSummonerId = -1;

# Full game stat hero
UPDATE GameSummaryStats
SET heroKills = 1,
  heroDeaths = 1,
  heroAssists = 1,
  heroWardsPlaced = 1,
  heroWardsKilled = 1
where GameSummaryStats.gameId = 1 and GameSummaryStats.heroSummonerId = 1;

# Villan Full game stats
UPDATE GameSummaryStats
SET villanKills = 1,
  villanDeaths = 1,
  villanAssists = 1,
  villanWardsPlaced = 1,
  villanWardsKilled = 1
where GameSummaryStats.gameId = 1 and GameSummaryStats.heroSummonerId = 1;
