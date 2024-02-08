create table if not exists social_media
(
    id            bigint not null auto_increment,
    owner_id      bigint,
    owner_type    varchar(255),
    media_type    varchar(255),
    name          varchar(255),
    logo_url      varchar(255),
    url           varchar(255),
    created_at    datetime(6),
    updated_at    datetime(6),
    primary key (id)
);

alter table social_media
    add constraint UNIQUE_OWNERTYPE_OWNERID unique (owner_id, owner_type, media_type);
