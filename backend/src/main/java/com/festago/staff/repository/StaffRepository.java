package com.festago.staff.repository;

import com.festago.festival.domain.Festival;
import com.festago.staff.domain.Staff;
import com.festago.staff.domain.StaffCode;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface StaffRepository extends JpaRepository<Staff, Long> {

    boolean existsByFestival(Festival festival);

    boolean existsByCode(StaffCode code);

    @Query("""
        SELECT sc
        FROM Staff sc
        LEFT JOIN FETCH sc.festival
        WHERE sc.code.value = :code
        """)
    Optional<Staff> findByCodeWithFetch(@Param("code") String code);
}
