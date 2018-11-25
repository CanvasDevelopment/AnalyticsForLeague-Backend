create table lol_analytics.ChampData
        (
        version    varchar(32)   null,
        id         varchar(32)   not null
        primary key,
        champ_key  varchar(32)   null,
        champ_name varchar(32)   null,
        title      varchar(64)   null,
        burb       varchar(1024) null
        );