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
)