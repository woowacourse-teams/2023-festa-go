alter table admin
    add constraint UNIQUE_USERNAME unique (username);

alter table member
    add constraint UNIQUE_SOCIAL unique (social_id, social_type);
