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
                     participantidentity.teamId = participant.TeamId
  LEFT JOIN timeline ON timeline.participantRowId = participant.Id
  LEFT JOIN creepspermin ON creepspermin.timelineId = timeline.Id
  LEFT JOIN xppermin ON xppermin.timelineId = timeline.Id
  LEFT JOIN goldpermin ON goldpermin.timelineId = timeline.Id
  LEFT JOIN damagetakenpermin ON damagetakenpermin.timelineId = timeline.Id
WHERE participantidentity.SummonerId = 1542360
      AND participantidentity.lane = 'Top'
      AND participantidentity.role = 'Solo';
