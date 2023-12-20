package com.festago.admin.repository;

import com.festago.admin.domain.Admin;
import java.util.Optional;
import org.springframework.data.repository.Repository;

public interface AdminRepository extends Repository<Admin, Long> {

    Admin save(Admin admin);

    Optional<Admin> findById(Long adminId);

    Optional<Admin> findByUsername(String username);

    boolean existsByUsername(String username);
}
