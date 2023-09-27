create table if not exists member_fcm
(
    id         bigint not null auto_increment,
    created_at datetime(6),
    updated_at datetime(6),
    member_id  bigint,
    fcm_token  varchar(255),
    primary key (id)
) engine innodb
  default charset = utf8mb4
  collate = utf8mb4_0900_ai_ci;

alter table member_fcm
    add constraint fk_member_fcm__member
        foreign key (member_id)
            references member (id);

alter table member_fcm
    add constraint unique_member_fcm unique (member_id, fcm_token);
