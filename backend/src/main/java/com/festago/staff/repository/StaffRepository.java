package com.festago.staff.repository;

import com.festago.festival.domain.Festival;
import com.festago.staff.domain.Staff;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface StaffRepository extends JpaRepository<Staff, Long> {

    boolean existsByFestival(Festival festival);

    @Query("""
        SELECT s
        FROM Staff s
        LEFT JOIN FETCH s.festival
        WHERE s.code.value = :code
        """)
    Optional<Staff> findByCodeWithFetch(@Param("code") String code);

    @Query("""
        SELECT s.code.value 
        FROM Staff s 
        WHERE s.code.value LIKE :prefix%
        """)
    List<String> findAllCodeByCodeStartsWith(@Param("prefix") String prefix);
}
