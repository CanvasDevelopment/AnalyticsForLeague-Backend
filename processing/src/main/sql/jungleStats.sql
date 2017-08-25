# Select the avg for each game stage for the delta stats using the summonerId

#Select avg number of wards placed
SELECT avg(wardsPlaced)
FROM (SELECT stats.wardsPlaced
      FROM participantidentity
        JOIN participant ON participantidentity.gameId = participant.GameId
                            AND participant.ParticipantId = participantidentity.ParticipantId
        JOIN stats ON stats.ParticipantRowId = participant.Id
      WHERE participantidentity.lane = 'JUNGLE' AND participantidentity.SummonerId = 1542360
      ORDER BY participant.gameId ASC
      LIMIT 20) AS Placed;

#max wards placed
SELECT max(stats.wardsPlaced)
FROM participantidentity
  JOIN participant ON participantidentity.gameId = participant.GameId
                      AND participant.ParticipantId = participantidentity.ParticipantId
  JOIN stats ON stats.ParticipantRowId = participant.Id
WHERE participantidentity.lane = 'JUNGLE' AND participantidentity.SummonerId = 1542360
ORDER BY participant.gameId ASC
LIMIT 20;

# My kda
SELECT
  stats.kills,
  stats.deaths,
  stats.assists
FROM participantidentity
  JOIN participant ON participantidentity.gameId = participant.GameId
                      AND participant.ParticipantId = participantidentity.ParticipantId
  JOIN stats ON stats.ParticipantRowId = participant.Id
WHERE participantidentity.lane = 'MIDDLE' AND participantidentity.SummonerId = 1542360
ORDER BY participant.gameId ASC;

# opponent kda.
SELECT
  stats.kills,
  stats.deaths,
  stats.assists
FROM participantidentity
  JOIN participant ON
                     participantidentity.gameId = participant.GameId AND
                     participant.lane = participantidentity.lane AND
                     participant.role = participantidentity.role AND
                     participantidentity.teamId != participant.TeamId
  JOIN stats ON stats.ParticipantRowId = participant.Id
WHERE participantidentity.lane = 'JUNGLE' AND participantidentity.SummonerId = 1542360
ORDER BY participant.gameId ASC;

# Enemy game stage stats when we are in a specific role, with a bit extra
SELECT
  creepspermin.zeroToTen * 10                 AS creepsperminEarlyGame,
  creepspermin.tenToTwenty * 10               AS creepsperminMidGame,
  creepspermin.twentyToThirty * 10            AS creepsperminLateGame,
  goldPerMin.zeroToTen * 10                 AS goldPerMinEarlyGame,
  goldPerMin.tenToTwenty * 10               AS goldPerMinMidGame,
  goldPerMin.twentyToThirty * 10            AS goldPerMinLateGame,
  xppermin.zeroToTen * 10                 AS xpperminEarlyGame,
  xppermin.tenToTwenty * 10               AS xpperminMidGame,
  xppermin.twentyToThirty * 10            AS xpperminLateGame,
  damagetakenpermin.zeroToTen * 10                 AS damagetakenperminEarlyGame,
  damagetakenpermin.tenToTwenty * 10               AS damagetakenperminMidGame,
  damagetakenpermin.twentyToThirty * 10            AS damagetakenperminLateGame,
  participant.GameId             AS GameId,
  participant.TeamId             AS TeamId,
  participant.ChampionId         AS EnemyChamp,
  participantidentity.championId AS HeroChamp,
  participant.lane               AS Lane,
  participant.role               AS Role
FROM participantidentity
  JOIN participant ON
                     participantidentity.gameId = participant.GameId AND
                     participant.lane = participantidentity.lane AND
                     participant.role = participantidentity.role AND
                     participantidentity.teamId != participant.TeamId
  LEFT JOIN timeline ON timeline.participantRowId = participant.Id
  LEFT JOIN creepspermin ON creepspermin.timelineId = timeline.Id
  LEFT JOIN xppermin ON xppermin.timelineId = timeline.Id
  LEFT JOIN goldpermin ON goldpermin.timelineId = timeline.Id
  LEFT JOIN damagetakenpermin ON damagetakenpermin.timelineId = timeline.Id
WHERE participantidentity.SummonerId = 1542360
      AND participantidentity.lane = 'Top'
      AND participantidentity.role = 'Solo';

# Our game stage stats in a specific role
SELECT
  avg(zeroToTen) * 10      AS EarlyGame,
  avg(tenToTwenty) * 10    AS MidGame,
  avg(twentyToThirty) * 10 AS LateGame
FROM participantidentity
  JOIN participant ON
                     participantidentity.gameId = participant.GameId AND
                     participant.ParticipantId = participantidentity.ParticipantId
  LEFT JOIN timeline ON timeline.participantRowId = participant.Id
  LEFT JOIN csdiffpermin ON csdiffpermin.timelineId = timeline.Id
WHERE participantidentity.SummonerId = 1542360
      AND participantidentity.lane = 'TOP'
      AND participantidentity.role = 'Solo';
# enemy adc game stats when im playing support
SELECT
  avg(zeroToTen) * 10      AS EarlyGame,
  avg(tenToTwenty) * 10    AS MidGame,
  avg(twentyToThirty) * 10 AS LateGame
FROM participantidentity
  JOIN participant ON
                     participantidentity.gameId = participant.GameId AND
                     participantidentity.teamId != participant.TeamId
  LEFT JOIN timeline ON timeline.participantRowId = participant.Id
  LEFT JOIN creepspermin ON creepspermin.timelineId = timeline.Id
WHERE participantidentity.SummonerId = 1542360
      AND participantidentity.lane = 'Bottom'
      AND participantidentity.role = 'DUO_SUPPORT'
      AND participant.lane = 'BOTTOM'
      AND participant.role = 'DUO_CARRY';

# enemy mid lane gold when im playing jungle
SELECT
  avg(zeroToTen) * 10      AS EarlyGame,
  avg(tenToTwenty) * 10    AS MidGame,
  avg(twentyToThirty) * 10 AS LateGame
FROM participantidentity
  JOIN participant ON
                     participantidentity.gameId = participant.GameId AND
                     participantidentity.teamId != participant.TeamId
  LEFT JOIN timeline ON timeline.participantRowId = participant.Id
  LEFT JOIN goldpermin ON goldpermin.timelineId = timeline.Id
WHERE participantidentity.SummonerId = 1542360
      AND participantidentity.lane = 'JUNGLE'
      AND participantidentity.role = 'NONE'
      AND participant.lane = 'TOP'
      AND participant.role = 'SOLO';

# ally mid lane gold when im playing jungle
SELECT
  avg(zeroToTen) * 10      AS EarlyGame,
  avg(tenToTwenty) * 10    AS MidGame,
  avg(twentyToThirty) * 10 AS LateGame
FROM participantidentity
  JOIN participant ON
                     participantidentity.gameId = participant.GameId AND
                     participantidentity.teamId = participant.TeamId
  LEFT JOIN timeline ON timeline.participantRowId = participant.Id
  LEFT JOIN goldpermin ON goldpermin.timelineId = timeline.Id
WHERE participantidentity.SummonerId = 1542360
      AND participantidentity.lane = 'JUNGLE'
      AND participantidentity.role = 'NONE'
      AND participant.lane = 'TOP'
      AND participant.role = 'SOLO';

# Ally adc when im playing support
SELECT
  avg(zeroToTen) * 10      AS EarlyGame,
  avg(tenToTwenty) * 10    AS MidGame,
  avg(twentyToThirty) * 10 AS LateGame
FROM participantidentity
  JOIN participant ON
                     participantidentity.gameId = participant.GameId AND
                     participantidentity.teamId = participant.TeamId
  LEFT JOIN timeline ON timeline.participantRowId = participant.Id
  LEFT JOIN creepspermin ON creepspermin.timelineId = timeline.Id
WHERE participantidentity.SummonerId = 1542360
      AND participantidentity.lane = 'Bottom'
      AND participantidentity.role = 'DUO_SUPPORT'
      AND participant.lane = 'BOTTOM'
      AND participant.role = 'DUO_CARRY';

# Our game stage stats in a specific role
SELECT
  avg(zeroToTen) * 10      AS EarlyGame,
  avg(tenToTwenty) * 10    AS MidGame,
  avg(twentyToThirty) * 10 AS LateGame
FROM participantidentity
  JOIN participant ON
                     participantidentity.gameId = participant.GameId AND
                     participant.ParticipantId = participantidentity.ParticipantId
  LEFT JOIN timeline ON timeline.participantRowId = participant.Id
  LEFT JOIN csdiffpermin ON csdiffpermin.timelineId = timeline.Id
WHERE participantidentity.SummonerId = 1542360
      AND participantidentity.lane = 'TOP'
      AND participantidentity.role = 'Solo';


SELECT participantidentity.gameId
FROM participantidentity
  JOIN participant
    ON participantidentity.gameId = participant.GameId AND participant.ParticipantId = participantidentity.ParticipantId
  LEFT JOIN timeline ON timeline.participantRowId = participant.Id
  LEFT JOIN creepspermin ON creepspermin.timelineId = timeline.Id
WHERE participantidentity.SummonerId = 1542360 AND participantidentity.lane = 'Bottom' AND
      participantidentity.role = 'DUO_CARRY' AND participantidentity.gameId;

SELECT participantidentity.gameId
FROM participantidentity
  JOIN participant ON participantidentity.gameId = participant.GameId AND participant.lane
                                                                          =
                                                                          participantidentity.lane
                      AND
                      participant.role
                      =
                      participantidentity.role
                      AND
                      participantidentity.teamId
                      !=
                      participant.TeamId
  LEFT JOIN timeline
    ON timeline.participantRowId =
       participant.Id
  LEFT JOIN creepspermin
    ON creepspermin.timelineId =
       timeline.Id
WHERE
  participantidentity.SummonerId =
  1542360 AND
  participantidentity.lane =
  'Bottom' AND
  participantidentity.role =
  'DUO_CARRY';

SELECT count(*)
FROM participantidentity
  JOIN participant ON
                     participantidentity.gameId = participant.GameId AND participant.lane = participantidentity.lane AND
                     participant.role = participantidentity.role AND participantidentity.teamId != participant.TeamId
  LEFT JOIN timeline ON timeline.participantRowId = participant.Id
  LEFT JOIN creepspermin ON creepspermin.timelineId = timeline.Id
WHERE participantidentity.SummonerId = 1542360 AND participantidentity.lane = 'Bottom' AND
      participantidentity.role = 'DUO_CARRY';

# get the avg for each game stage for the delta stats for our opposite lane
SELECT count(*)
FROM participantidentity
  INNER JOIN participant ON participantidentity.gameId = participant.GameId
  INNER JOIN timeline ON timeline.participantRowId = participant.Id
  INNER JOIN creepspermin ON creepspermin.timelineId = timeline.Id
WHERE participantidentity.SummonerId = 1542360 AND participant.SummonerId != 1542360 AND lane = 'TOP';
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

# Total dragons, Rift Heralds and Barons for enemy team when im playing jungle.
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
  avg(DragonKills) AS  DragonsPerGame,
  avg(BaronKills)      BaronsPerGame,
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
  avg(DragonKills) AS  DragonsPerGame,
  avg(BaronKills)      BaronsPerGame,
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
  JOIN stats ON stats.ParticipantRowId = participant.Id
  JOIN creepspermin ON creepspermin.timelineId = timeline.Id
WHERE accountId = 200774483 AND lane = 'JUNGLE';

#kills, deaths assists avg
SELECT
  avg(kills),
  avg(deaths),
  avg(assists)
FROM participantidentity
  JOIN player ON player.ParticipantIdentityRowId = participantidentity.Id
  JOIN participant
    ON participantidentity.gameId = participant.GameId AND participant.ParticipantId = participantidentity.ParticipantId

  JOIN timeline ON timeline.participantRowId = participant.Id
  JOIN stats ON stats.ParticipantRowId = participant.Id
  JOIN creepspermin ON creepspermin.timelineId = timeline.Id
WHERE participant.SummonerId = 1542360 AND lane = 'JUNGLE';

SELECT count(*)
FROM participantidentity
  JOIN player ON player.ParticipantIdentityRowId = participantidentity.Id
  JOIN participant
    ON participantidentity.gameId = participant.GameId AND participant.ParticipantId = participantidentity.ParticipantId
  JOIN team ON team.GameId = participantidentity.gameId AND team.TeamId != participant.TeamId
  JOIN timeline ON timeline.participantRowId = participant.Id
  JOIN stats ON stats.ParticipantRowId = participant.Id
  JOIN creepspermin ON creepspermin.timelineId = timeline.Id
WHERE accountId = 200774483 AND lane = 'JUNGLE';

#turret kills
#inhub kills
#wards placed
#wards killed
#first blood kill
#first blood assist