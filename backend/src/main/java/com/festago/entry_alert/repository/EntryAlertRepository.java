package com.festago.entry_alert.repository;

import com.festago.entry_alert.domain.EntryAlert;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EntryAlertRepository extends JpaRepository<EntryAlert, Long> {

}
