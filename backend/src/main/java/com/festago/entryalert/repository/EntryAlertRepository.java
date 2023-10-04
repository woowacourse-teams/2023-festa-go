package com.festago.entryalert.repository;

import com.festago.entryalert.domain.AlertStatus;
import com.festago.entryalert.domain.EntryAlert;
import jakarta.persistence.LockModeType;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface EntryAlertRepository extends JpaRepository<EntryAlert, Long> {

    @Query("SELECT ea FROM EntryAlert ea WHERE ea.status = :status")
    List<EntryAlert> findAllByStatus(@Param("status") AlertStatus status);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT ea FROM EntryAlert ea WHERE ea.id = :id and ea.status = :status")
    Optional<EntryAlert> findByIdAndStatusForUpdate(@Param("id") Long id, @Param("status") AlertStatus status);
}
