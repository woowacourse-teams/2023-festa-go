package com.festago.staff.repository;

import com.festago.festival.domain.Festival;
import com.festago.staff.domain.StaffCode;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface StaffCodeRepository extends JpaRepository<StaffCode, Long> {

    boolean existsByFestival(Festival festival);

    boolean existsByCode(String code);

    @Query("""
        SELECT sc
        FROM StaffCode sc
        LEFT JOIN FETCH sc.festival
        WHERE sc.code = :code
        """)
    Optional<StaffCode> findByCodeWithFetch(@Param("code") String code);
}
