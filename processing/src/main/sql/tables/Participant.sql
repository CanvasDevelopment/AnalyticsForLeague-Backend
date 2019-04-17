Create TABLE Participant(
  Id Integer AUTO_INCREMENT,
  ParticipantId INTEGER,
  TeamId INTEGER,
  ChampionId INTEGER,
  Spell1Id INTEGER,
  Spell2Id INTEGER,
  SummonerId VARCHAR(64),
  HighestSeasonAchievedTier VARCHAR(32),
  GameId BIGINT,
  Role VARCHAR(16),
  Lane VARCHAR(16)
)