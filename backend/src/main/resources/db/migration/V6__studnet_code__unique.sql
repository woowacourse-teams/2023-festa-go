alter table student_code
    add constraint unique_username__school unique (username, school_id);

alter table student_code
    add column issued_at datetime(6);
