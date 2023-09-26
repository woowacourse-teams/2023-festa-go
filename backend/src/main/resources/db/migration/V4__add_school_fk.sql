alter table ticket
    add column school_id bigint not null;

alter table ticket
    add constraint fk_ticket__school
        foreign key (school_id)
            references school (id);

alter table festival
    add column school_id bigint;

alter table festival
    add constraint fk_festival__school
        foreign key (school_id)
            references school (id);
