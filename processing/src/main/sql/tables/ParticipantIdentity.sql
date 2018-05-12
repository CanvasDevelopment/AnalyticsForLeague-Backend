CREATE TABLE ParticipantIdentity(
  ParticipantId INTEGER PRIMARY KEY,
  SummonerId BIGINT,
  GameId BIGINT,
  TeamId INTEGER,
  Role VARCHAR(16),
  Lane VARCHAR(16)
)