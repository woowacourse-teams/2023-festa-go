package com.festago.domain;

import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentCodeRepository extends JpaRepository<StudentCode, Long> {

    void deleteByMember(Member member);
}
