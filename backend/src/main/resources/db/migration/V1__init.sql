create table if not exists festival
(
    id         bigint not null auto_increment,
    created_at datetime(6),
    updated_at datetime(6),
    end_date   date,
    name       varchar(255),
    start_date date,
    thumbnail  varchar(255),
    primary key (id)
) engine innodb
  default charset = utf8mb4
  collate = utf8mb4_0900_ai_ci;

create table if not exists member
(
    id            bigint not null auto_increment,
    created_at    datetime(6),
    updated_at    datetime(6),
    deleted_at    datetime(6),
    nickname      varchar(255),
    profile_image varchar(255),
    social_id     varchar(255),
    social_type   varchar(255),
    primary key (id)
) engine innodb
  default charset = utf8mb4
  collate = utf8mb4_0900_ai_ci;

create table if not exists member_ticket
(
    id          bigint  not null auto_increment,
    created_at  datetime(6),
    updated_at  datetime(6),
    entry_state varchar(255),
    entry_time  datetime(6),
    number      integer not null,
    ticket_type varchar(255),
    owner_id    bigint,
    stage_id    bigint,
    primary key (id)
) engine innodb
  default charset = utf8mb4
  collate = utf8mb4_0900_ai_ci;

create table if not exists stage
(
    id               bigint      not null auto_increment,
    created_at       datetime(6),
    updated_at       datetime(6),
    line_up          varchar(255),
    start_time       datetime(6) not null,
    ticket_open_time datetime(6),
    festival_id      bigint,
    primary key (id)
) engine innodb
  default charset = utf8mb4
  collate = utf8mb4_0900_ai_ci;

create table if not exists ticket
(
    id          bigint not null auto_increment,
    created_at  datetime(6),
    updated_at  datetime(6),
    ticket_type varchar(255),
    stage_id    bigint,
    primary key (id)
) engine innodb
  default charset = utf8mb4
  collate = utf8mb4_0900_ai_ci;

create table if not exists ticket_amount
(
    ticket_id       bigint  not null,
    created_at      datetime(6),
    updated_at      datetime(6),
    reserved_amount integer not null,
    total_amount    integer not null,
    primary key (ticket_id)
) engine innodb
  default charset = utf8mb4
  collate = utf8mb4_0900_ai_ci;

create table if not exists ticket_entry_time
(
    id         bigint not null auto_increment,
    created_at datetime(6),
    updated_at datetime(6),
    amount     integer,
    entry_time datetime(6),
    ticket_id  bigint,
    primary key (id)
) engine innodb
  default charset = utf8mb4
  collate = utf8mb4_0900_ai_ci;

create table if not exists school
(
    id         bigint       not null auto_increment,
    created_at datetime     null,
    updated_at datetime     null,
    domain     varchar(50)  not null,
    name       varchar(255) not null,
    primary key (id)
) engine innodb
  default charset = utf8mb4
  collate = utf8mb4_0900_ai_ci;

create table if not exists student
(
    id         bigint       not null auto_increment,
    created_at datetime     null,
    updated_at datetime     null,
    username   varchar(255) not null,
    member_id  bigint       not null,
    school_id  bigint       not null,
    primary key (id)
) engine innodb
  default charset = utf8mb4
  collate = utf8mb4_0900_ai_ci;

create table if not exists student_code
(
    id         bigint auto_increment not null,
    created_at datetime              null,
    updated_at datetime              null,
    school_id  bigint                null,
    member_id  bigint                null,
    code       varchar(255)          null,
    primary key (id)
) engine innodb
  default charset = utf8mb4
  collate = utf8mb4_0900_ai_ci;

create table if not exists admin
(
    id         bigint auto_increment not null,
    created_at datetime              null,
    updated_at datetime              null,
    username   varchar(255)          null,
    password   varchar(255)          null,
    primary key (id)
) engine innodb
  default charset = utf8mb4
  collate = utf8mb4_0900_ai_ci;

alter table member_ticket
    add constraint fk_member_ticket__member
        foreign key (owner_id)
            references member (id);

alter table member_ticket
    add constraint fk_member_ticket__stage
        foreign key (stage_id)
            references stage (id);

alter table stage
    add constraint fk_stage__festival
        foreign key (festival_id)
            references festival (id);

alter table ticket
    add constraint fk_ticket__stage
        foreign key (stage_id)
            references stage (id);

alter table ticket_amount
    add constraint fk_ticket_amount__ticket
        foreign key (ticket_id)
            references ticket (id);

alter table ticket_entry_time
    add constraint fk_ticket_entry_time__ticket
        foreign key (ticket_id)
            references ticket (id);

alter table student
    add constraint fk_student__member
        foreign key (member_id)
            references member (id);

alter table student
    add constraint fk_student__school
        foreign key (school_id)
            references school (id);

alter table student_code
    add constraint fk_student_code__member
        foreign key (member_id)
            references member (id);

alter table student_code
    add constraint fk_student_code__school
        foreign key (school_id)
            references school (id);
