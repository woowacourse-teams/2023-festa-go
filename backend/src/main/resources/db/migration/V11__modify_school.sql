alter table school
    add constraint UNIQUE_DOMAIN unique (domain);

alter table school
    add constraint UNIQUE_NAME unique (name);

create table if not exists festival_info
(
    festival_id       bigint  not null,
    created_at      datetime(6),
    updated_at      datetime(6),
    artist_info     varchar(500) not null,
    primary key (festival_id)
) engine innodb
  default charset = utf8mb4
  collate = utf8mb4_0900_ai_ci;


alter table festival_info
    add constraint fk_festival_info__festival
        foreign key (festival_id)
            references festival (id);
