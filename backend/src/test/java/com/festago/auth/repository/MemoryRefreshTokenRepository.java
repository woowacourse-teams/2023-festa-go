package com.festago.auth.repository;

import com.festago.auth.domain.RefreshToken;
import java.util.HashMap;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

public class MemoryRefreshTokenRepository implements RefreshTokenRepository {

    private final HashMap<UUID, RefreshToken> memory = new HashMap<>();

    @Override
    public RefreshToken save(RefreshToken refreshToken) {
        memory.put(refreshToken.getId(), refreshToken);
        return refreshToken;
    }

    @Override
    public Optional<RefreshToken> findById(UUID id) {
        return Optional.ofNullable(memory.get(id));
    }

    @Override
    public void deleteByMemberId(Long memberId) {
        memory.entrySet().removeIf(it -> Objects.equals(it.getValue().getMemberId(), memberId));
    }
}
