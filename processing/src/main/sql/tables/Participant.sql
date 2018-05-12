Create TABLE Participant(
  ParticipantId INTEGER PRIMARY KEY,
  TeamId INTEGER,
  ChampionId INTEGER,
  Spell1Id INTEGER,
  Spell2Id INTEGER,
  SummonerId BIGINT,
  HighestSeasonAchievedTier VARCHAR(32),
  GameId BIGINT,
  Role VARCHAR(16),
  Lane VARCHAR(16)
)