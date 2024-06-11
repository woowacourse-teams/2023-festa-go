create table if not exists new_ticket
(
    id                 bigint auto_increment primary key,
    dtype              varchar(10) not null,
    school_id          bigint      not null,
    ticket_exclusive   varchar(10) not null,
    amount             int         not null,
    max_reserve_amount int         not null,
    created_at         datetime(6) not null,
    updated_at         datetime(6) not null
);

create table if not exists stage_ticket
(
    id       bigint primary key,
    stage_id bigint not null,
    constraint fk_stage_ticket__new_ticket
        foreign key (id) references new_ticket (id),
    constraint fk_stage_ticket__stage
        foreign key (stage_id) references stage (id)
);

create table if not exists reserve_ticket
(
    id          bigint auto_increment primary key,
    member_id   bigint      not null,
    ticket_id   bigint      not null,
    sequence    int         not null,
    entry_time  datetime(6) not null,
    entry_state varchar(15) not null,
    ticket_type varchar(10) not null,
    created_at  datetime(6) not null,
    updated_at  datetime(6) not null,
    constraint fk_reserve_ticket__member
        foreign key (member_id) references member (id),
    constraint fk_reserve_ticket__new_ticket
        foreign key (ticket_id) references new_ticket (id)
);

create index index_reserve_ticket_member_id_ticket_id
    on reserve_ticket (member_id, ticket_id);

create table if not exists stage_ticket_entry_time
(
    id              bigint auto_increment primary key,
    stage_ticket_id bigint      not null,
    entry_time      datetime(6) not null,
    amount          int         not null,
    created_at      datetime(6) not null,
    updated_at      datetime(6) not null,
    constraint fk_stage_ticket_entry_time__stage_ticket
        foreign key (stage_ticket_id) references stage_ticket (id)
);
