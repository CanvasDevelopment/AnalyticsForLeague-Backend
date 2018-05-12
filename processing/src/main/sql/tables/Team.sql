Create TABLE Team(
  Id INTEGER PRIMARY KEY AUTO_INCREMENT,
  TeamId INTEGER,
  Win BOOLEAN,
  FirstBlood BOOLEAN,
  FirstTower BOOLEAN,
  FirstInhibitor BOOLEAN,
  FirstBaron BOOLEAN,
  FirstDragon BOOLEAN,
  FirstRiftHerald BOOLEAN,
  TowerKills INTEGER,
  InhibitorKills INTEGER,
  BaronKills INTEGER,
  DragonKills INTEGER,
  VileMawKills INTEGER,
  RiftHeraldKills INTEGER,
  DominionVictoryScore INTEGER,
  GameId BIGINT
)