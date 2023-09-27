create index temp_member_index
    on student_code (member_id);

drop index fk_student_code__member on student_code;

create unique index fk_student_code__member
    on student_code (member_id);

drop index temp_member_index on student_code;

alter table student_code
    add column issued_at datetime(6);

alter table student_code
    drop column created_at;

alter table student_code
    drop column updated_at;
