create table if not exists artist
(
    id            bigint not null auto_increment,
    name          varchar(50),
    profile_image varchar(512),
    created_at    datetime(6),
    updated_at    datetime(6),
    primary key (id)
);

create table if not exists stage_artist
(
    id         bigint not null auto_increment,
    stage_id   bigint not null,
    artist_id  bigint not null,
    created_at datetime(6),
    updated_at datetime(6),
    primary key (id)
);

alter table stage_artist
    add constraint fk_stage_artist__stage
        foreign key (stage_id)
            references stage (id);

alter table stage_artist
    add constraint fk_stage_artist__artist
        foreign key (artist_id)
            references artist (id);

alter table school
    add column region varchar(20) not null
        default '서울';
