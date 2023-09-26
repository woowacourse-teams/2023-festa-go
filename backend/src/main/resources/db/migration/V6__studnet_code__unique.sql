alter table student_code
    add constraint unique_username__school unique (username, school_id);

