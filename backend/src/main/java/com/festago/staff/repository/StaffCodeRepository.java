package com.festago.staff.repository;

import com.festago.festival.domain.Festival;
import com.festago.staff.domain.StaffCode;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StaffCodeRepository extends JpaRepository<StaffCode, Long> {

    boolean existsByFestival(Festival festival);

    boolean existsByCode(String value);
}
