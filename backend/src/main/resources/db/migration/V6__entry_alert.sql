create table if not exists entry_alert
(
    id bigint not null auto_increment,
    entry_time datetime(6) not null,
    stage_id bigint not null,
    primary key (id)
) engine innodb
    default charset = utf8mb4
    collate = utf8mb4_0900_ai_ci;
