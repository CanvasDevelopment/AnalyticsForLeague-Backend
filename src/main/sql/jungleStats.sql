# Select the avg for each game stage for the delta stats
SELECT
  avg(zeroToTen) * 10      AS EarlyGame,
  avg(tenToTwenty) * 10    AS MidGame,
  avg(twentyToThirty) * 10 AS LateGame
FROM participantidentity
  JOIN player ON player.ParticipantIdentityRowId = participantidentity.Id
  JOIN participant ON participantidentity.gameId = participant.GameId
                      AND participant.ParticipantId = participantidentity.ParticipantId
  JOIN timeline ON timeline.participantRowId = participant.Id
  JOIN creepspermin ON creepspermin.timelineId = timeline.Id
WHERE accountId = 200774483 AND lane = 'TOP';

# get a list of results, not an average
SELECT
  zeroToTen * 10      AS EarlyGame,
  tenToTwenty * 10    AS MidGame,
  twentyToThirty * 10 AS LateGame
FROM participantidentity
  JOIN player ON player.ParticipantIdentityRowId = participantidentity.Id
  JOIN participant ON participantidentity.gameId = participant.GameId
                      AND participant.ParticipantId = participantidentity.ParticipantId
  JOIN timeline ON timeline.participantRowId = participant.Id
  JOIN creepspermin ON creepspermin.timelineId = timeline.Id
WHERE accountId = 200774483 AND lane = 'BOTTOM';

#First dragon baron, and rift herald for team. Note that this may count times where no one gets the dragon/baron/rift herald etc.
SELECT
  FirstDragon,
  FirstBaron,
  FirstRiftHerald
FROM participantidentity
  JOIN player ON player.ParticipantIdentityRowId = participantidentity.Id
  JOIN participant ON participantidentity.gameId = participant.GameId
                      AND participant.ParticipantId = participantidentity.ParticipantId
  JOIN team ON team.GameId = participantidentity.gameId AND team.TeamId = participant.TeamId
  JOIN timeline ON timeline.participantRowId = participant.Id
  JOIN creepspermin ON creepspermin.timelineId = timeline.Id
WHERE accountId = 200774483 AND lane = 'JUNGLE';

# Total dragons, Rift Heralds and Barons for ally team when playing jungle.
SELECT
  DragonKills,
  BaronKills,
  RiftHeraldKills
FROM participantidentity
  JOIN player ON player.ParticipantIdentityRowId = participantidentity.Id
  JOIN participant ON participantidentity.gameId = participant.GameId
                      AND participant.ParticipantId = participantidentity.ParticipantId
  JOIN team ON team.GameId = participantidentity.gameId AND team.TeamId = participant.TeamId
  JOIN timeline ON timeline.participantRowId = participant.Id
  JOIN creepspermin ON creepspermin.timelineId = timeline.Id
WHERE accountId = 200774483 AND lane = 'JUNGLE';

# Total dragons, Rift Heralds and Barons for enemy team when playing jungle.
SELECT
  DragonKills,
  BaronKills,
  RiftHeraldKills
FROM participantidentity
  JOIN player ON player.ParticipantIdentityRowId = participantidentity.Id
  JOIN participant ON participantidentity.gameId = participant.GameId
                      AND participant.ParticipantId = participantidentity.ParticipantId
  JOIN team ON team.GameId = participantidentity.gameId AND team.TeamId != participant.TeamId
  JOIN timeline ON timeline.participantRowId = participant.Id
  JOIN creepspermin ON creepspermin.timelineId = timeline.Id
WHERE accountId = 200774483 AND lane = 'JUNGLE';

# avg baron, dragon and rift herald kills for team
SELECT
  avg(DragonKills) as DragonsPerGame,
  avg(BaronKills) BaronsPerGame,
  avg(RiftHeraldKills) RiftHeraldsPerGame
FROM participantidentity
  JOIN player ON player.ParticipantIdentityRowId = participantidentity.Id
  JOIN participant ON participantidentity.gameId = participant.GameId
                      AND participant.ParticipantId = participantidentity.ParticipantId
  JOIN team ON team.GameId = participantidentity.gameId AND team.TeamId = participant.TeamId
  JOIN timeline ON timeline.participantRowId = participant.Id
  JOIN creepspermin ON creepspermin.timelineId = timeline.Id
WHERE accountId = 200774483 AND lane = 'JUNGLE';

# avg baron, dragon and rift herald kills for enemy team.
SELECT
  avg(DragonKills) as DragonsPerGame,
  avg(BaronKills) BaronsPerGame,
  avg(RiftHeraldKills) RiftHeraldsPerGame
FROM participantidentity
  JOIN player ON player.ParticipantIdentityRowId = participantidentity.Id
  JOIN participant ON participantidentity.gameId = participant.GameId
                      AND participant.ParticipantId = participantidentity.ParticipantId
  JOIN team ON team.GameId = participantidentity.gameId AND team.TeamId != participant.TeamId
  JOIN timeline ON timeline.participantRowId = participant.Id
  JOIN creepspermin ON creepspermin.timelineId = timeline.Id
WHERE accountId = 200774483 AND lane = 'JUNGLE';

#kills, deaths assists list
SELECT
  kills,
  deaths,
  assists
FROM participantidentity
  JOIN player ON player.ParticipantIdentityRowId = participantidentity.Id
  JOIN participant ON participantidentity.gameId = participant.GameId
                      AND participant.ParticipantId = participantidentity.ParticipantId
  JOIN timeline ON timeline.participantRowId = participant.Id
  join stats on stats.ParticipantRowId = participant.Id
  JOIN creepspermin ON creepspermin.timelineId = timeline.Id
WHERE accountId = 200774483 AND lane = 'JUNGLE';


#kills, deaths assists avg
SELECT
  avg(kills),
  avg(deaths),
  avg(assists)
FROM participantidentity
  JOIN player ON player.ParticipantIdentityRowId = participantidentity.Id
  JOIN participant ON participantidentity.gameId = participant.GameId

  JOIN timeline ON timeline.participantRowId = participant.Id
  join stats on stats.ParticipantRowId = participant.Id
  JOIN creepspermin ON creepspermin.timelineId = timeline.Id
WHERE player.accountId = 200774483 AND lane = 'JUNGLE';
