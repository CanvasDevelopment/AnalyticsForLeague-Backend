CREATE TABLE MatchSummary(
  PlatformId VARCHAR(64),
  GameId BIGINT,
  Champion INTEGER,
  Queue VARCHAR(32),
  Season VARCHAR(32),
  TIMESTAMP BIGINT,
  Role VARCHAR(16),
  Lane VARCHAR(16),
  SummonerId BIGINT
)