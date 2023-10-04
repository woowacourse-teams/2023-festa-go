create table if not exists entry_alert
(
    id bigint not null auto_increment,
    created_at datetime(6),
    updated_at datetime(6),
    entry_time datetime(6) not null,
    stage_id bigint not null,
    status varchar(255) not null,
    primary key (id)
) engine innodb
    default charset = utf8mb4
    collate = utf8mb4_0900_ai_ci;
