CREATE TABLE Timeline(
  Id INTEGER AUTO_INCREMENT PRIMARY KEY,
  ParticipantId INTEGER,
  ParticipantRowId INTEGER,
  Role VARCHAR(16),
  Lane VARCHAR(16)
)