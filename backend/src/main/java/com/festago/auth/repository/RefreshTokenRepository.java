package com.festago.auth.repository;

import com.festago.auth.domain.RefreshToken;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.repository.Repository;

public interface RefreshTokenRepository extends Repository<RefreshToken, UUID> {

    RefreshToken save(RefreshToken refreshToken);

    Optional<RefreshToken> findById(UUID id);

    void deleteByMemberId(Long memberId);
}
