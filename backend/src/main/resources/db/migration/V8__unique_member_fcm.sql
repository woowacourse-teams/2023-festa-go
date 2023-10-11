alter table member_fcm
    add constraint unique_member_fcm unique (member_id, fcm_token);
