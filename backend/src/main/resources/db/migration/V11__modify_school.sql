alter table school
    add constraint UNIQUE_DOMAIN unique (domain);

alter table school
    add constraint UNIQUE_NAME unique (name);

create table if not exists festival_query_info
(
    id              bigint not null auto_increment,
    festival_id     bigint  not null,
    created_at      datetime(6),
    updated_at      datetime(6),
    artist_info     text not null,
    primary key (id)
) engine innodb
  default charset = utf8mb4
  collate = utf8mb4_0900_ai_ci;
