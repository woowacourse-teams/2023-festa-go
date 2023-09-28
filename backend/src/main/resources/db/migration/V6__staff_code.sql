create table staff_code
(
    id          bigint not null auto_increment,
    code        varchar(255),
    festival_id bigint,
    created_at  datetime(6),
    updated_at  datetime(6),
    primary key (id)
) engine innodb
  default charset = utf8mb4
  collate = utf8mb4_0900_ai_ci;


alter table staff_code
    add constraint fk_staff_code__festival
        foreign key (festival_id)
            references festival (id);

