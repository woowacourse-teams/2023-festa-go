create table if not exists bookmark
(
    id            bigint not null auto_increment,
    bookmark_type varchar(255),
    resource_id   bigint,
    member_id     bigint,
    created_at    datetime(6),
    updated_at    datetime(6),
    primary key (id)
);

alter table bookmark
    add constraint UNIQUE_BOOKMARK_TYPE_RESOURCE_ID_MEMBER_ID unique (bookmark_type, resource_id, member_id);

create index bookmark_member_id_bookmark_type
    on bookmark (member_id, bookmark_type);
