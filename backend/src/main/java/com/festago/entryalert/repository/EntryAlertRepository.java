package com.festago.entryalert.repository;

import com.festago.entryalert.domain.EntryAlert;
import jakarta.persistence.LockModeType;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface EntryAlertRepository extends JpaRepository<EntryAlert, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT ea FROM EntryAlert ea WHERE ea.id = :id")
    Optional<EntryAlert> findByIdForUpdate(@Param("id") Long id);
}
