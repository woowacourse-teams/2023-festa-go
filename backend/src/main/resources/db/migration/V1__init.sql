create table if not exists admin
(
    id         bigint auto_increment primary key,
    created_at datetime     null,
    updated_at datetime     null,
    username   varchar(20)  null,
    password   varchar(255) null,
    constraint unique_username
        unique (username)
);

create table if not exists artist
(
    id                   bigint auto_increment primary key,
    name                 varchar(20)  null,
    profile_image_url    varchar(512) null,
    created_at           datetime(6)  null,
    updated_at           datetime(6)  null,
    background_image_url varchar(512) null
);

create table if not exists bookmark
(
    id            bigint auto_increment primary key,
    bookmark_type varchar(10) null,
    resource_id   bigint      null,
    member_id     bigint      null,
    created_at    datetime(6) null,
    updated_at    datetime(6) null,
    constraint unique_bookmark_type_resource_id_member_id
        unique (bookmark_type, resource_id, member_id)
);

create index index_bookmark__member_id_bookmark_type
    on bookmark (member_id, bookmark_type);

create table if not exists festival_query_info
(
    id          bigint auto_increment primary key,
    festival_id bigint      not null,
    created_at  datetime(6) null,
    updated_at  datetime(6) null,
    artist_info text        not null,
    constraint unique_festival_id
        unique (festival_id)
);

create table if not exists member
(
    id                bigint auto_increment primary key,
    created_at        datetime(6)  null,
    updated_at        datetime(6)  null,
    deleted_at        datetime(6)  null,
    nickname          varchar(20)  null,
    profile_image_url varchar(512) null,
    social_id         varchar(40)  null,
    social_type       varchar(10)  null,
    constraint unique_social_id_social_type
        unique (social_id, social_type)
);

create table if not exists member_fcm
(
    id         bigint auto_increment primary key,
    created_at datetime(6)  null,
    updated_at datetime(6)  null,
    member_id  bigint       null,
    fcm_token  varchar(255) null,
    constraint unique_member_id_fcm_token
        unique (member_id, fcm_token),
    constraint fk_member_fcm__member
        foreign key (member_id) references member (id)
);

create table if not exists school
(
    id                   bigint auto_increment primary key,
    created_at           datetime     null,
    updated_at           datetime     null,
    domain               varchar(25)  not null,
    name                 varchar(20)  not null,
    region               varchar(20)  not null,
    logo_url             varchar(512) null,
    background_image_url varchar(512) null,
    constraint unique_domain
        unique (domain),
    constraint unique_name
        unique (name)
);

create table if not exists festival
(
    id               bigint auto_increment primary key,
    created_at       datetime(6)  null,
    updated_at       datetime(6)  null,
    end_date         date         null,
    name             varchar(50)  null,
    start_date       date         null,
    poster_image_url varchar(512) null,
    school_id        bigint       not null,
    constraint fk_festival__school
        foreign key (school_id) references school (id)
);

create index index_festival_end_date_desc
    on festival (end_date desc);

create index index_festival_start_date_desc
    on festival (start_date desc);

create index index_festival_start_date
    on festival (start_date);

create table if not exists social_media
(
    id         bigint auto_increment primary key,
    owner_id   bigint       null,
    owner_type varchar(10)  null,
    media_type varchar(12)  null,
    name       varchar(50)  null,
    logo_url   varchar(512) null,
    url        varchar(512) null,
    created_at datetime(6)  null,
    updated_at datetime(6)  null,
    constraint unique_owner_id_owner_type_media_type
        unique (owner_id, owner_type, media_type)
);

create table if not exists stage
(
    id               bigint auto_increment primary key,
    created_at       datetime(6) null,
    updated_at       datetime(6) null,
    start_time       datetime(6) not null,
    ticket_open_time datetime(6) null,
    festival_id      bigint      null,
    constraint fk_stage__festival
        foreign key (festival_id) references festival (id)
);

create table if not exists member_ticket
(
    id          bigint auto_increment primary key,
    created_at  datetime(6) null,
    updated_at  datetime(6) null,
    entry_state varchar(15) null,
    entry_time  datetime(6) null,
    number      int         not null,
    ticket_type varchar(10) null,
    owner_id    bigint      null,
    stage_id    bigint      null,
    constraint fk_member_ticket__member
        foreign key (owner_id) references member (id),
    constraint fk_member_ticket__stage
        foreign key (stage_id) references stage (id)
);

create table if not exists stage_artist
(
    id         bigint auto_increment primary key,
    stage_id   bigint      not null,
    artist_id  bigint      not null,
    created_at datetime(6) null,
    updated_at datetime(6) null,
    constraint fk_stage_artist__artist
        foreign key (artist_id) references artist (id),
    constraint fk_stage_artist__stage
        foreign key (stage_id) references stage (id)
);

create table if not exists stage_query_info
(
    id          bigint auto_increment primary key,
    stage_id    bigint      not null,
    created_at  datetime(6) null,
    updated_at  datetime(6) null,
    artist_info text        not null,
    constraint unique_stage_id
        unique (stage_id)
);

create table if not exists student
(
    id         bigint auto_increment primary key,
    created_at datetime    null,
    updated_at datetime    null,
    username   varchar(50) not null,
    member_id  bigint      not null,
    school_id  bigint      not null,
    constraint fk_student__member
        foreign key (member_id) references member (id),
    constraint fk_student__school
        foreign key (school_id) references school (id)
);

create table if not exists student_code
(
    id        bigint auto_increment primary key,
    school_id bigint      null,
    member_id bigint      null,
    code      varchar(50) null,
    username  varchar(50) null,
    issued_at datetime(6) not null,
    constraint unique_member_id
        unique (member_id),
    constraint fk_student_code__member
        foreign key (member_id) references member (id),
    constraint fk_student_code__school
        foreign key (school_id) references school (id)
);

create table if not exists ticket
(
    id          bigint auto_increment primary key,
    created_at  datetime(6) null,
    updated_at  datetime(6) null,
    ticket_type varchar(10) null,
    stage_id    bigint      null,
    school_id   bigint      not null,
    constraint fk_ticket__school
        foreign key (school_id) references school (id),
    constraint fk_ticket__stage
        foreign key (stage_id) references stage (id)
);

create table if not exists ticket_amount
(
    ticket_id       bigint      not null primary key,
    created_at      datetime(6) null,
    updated_at      datetime(6) null,
    reserved_amount int         not null,
    total_amount    int         not null,
    constraint fk_ticket_amount__ticket
        foreign key (ticket_id) references ticket (id)
);

create table if not exists ticket_entry_time
(
    id         bigint auto_increment primary key,
    created_at datetime(6) null,
    updated_at datetime(6) null,
    amount     int         null,
    entry_time datetime(6) null,
    ticket_id  bigint      null,
    constraint fk_ticket_entry_time__ticket
        foreign key (ticket_id) references ticket (id)
);

create table if not exists refresh_token
(
    id         binary(16) primary key,
    member_id  bigint      not null,
    expired_at datetime(6) not null
);
