-- issued_at 칼럼 추가 (NOT NULL)
alter table student_code
    add column issued_at datetime(6) not null
    default '1999-12-31 00:00:00';

alter table student_code
    alter column issued_at drop default;

-- 기존 created_at updated_at 삭제
alter table student_code
    drop column created_at;

alter table student_code
    drop column updated_at;

-- StudentCode의 member_id UNIQUE 제약조건 추가
alter table student_code
    modify column member_id bigint unique;
정
