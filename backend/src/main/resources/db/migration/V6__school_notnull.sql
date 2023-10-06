alter table festival
    modify school_id bigint not null;

alter table ticket
    modify school_id bigint not null;
