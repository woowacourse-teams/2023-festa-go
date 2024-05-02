CREATE TABLE upload_file
(
    id         binary(16)   not null primary key,
    created_at datetime(6)  not null,
    updated_at datetime(6)  not null,
    size       bigint       not null,
    location   varchar(255) not null,
    status     varchar(15)  not null,
    extension  varchar(6)   not null,
    owner_type varchar(12)  null,
    owner_id   bigint       null
);

create index index_upload_file__owner_id_owner_type
    on upload_file (owner_id, owner_type);
