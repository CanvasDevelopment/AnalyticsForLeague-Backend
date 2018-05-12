CREATE TABLE Player(
  Id INTEGER AUTO_INCREMENT PRIMARY KEY ,
  ParticipantIdentityRowId BIGINT,
  PlatformId VARCHAR(32),
  AccountId BIGINT,
  SummonerName VARCHAR(64),
  currentPlatformId VARCHAR(32),
  matchHistoryUri VARCHAR(64),
  profileIcon INTEGER
)