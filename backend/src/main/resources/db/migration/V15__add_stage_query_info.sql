create table if not exists stage_query_info
(
    id          bigint not null auto_increment,
    stage_id    bigint not null,
    created_at  datetime(6),
    updated_at  datetime(6),
    artist_info text   not null,
    primary key (id)
) engine innodb
  default charset = utf8mb4
  collate = utf8mb4_0900_ai_ci;

alter table stage_query_info
    add constraint UNIQUE_STAGE_ID unique (stage_id);
