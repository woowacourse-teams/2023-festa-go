package com.festago.auth.domain;

import com.festago.common.domain.BaseTimeEntity;
import jakarta.annotation.Nonnull;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Persistable;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RefreshToken extends BaseTimeEntity implements Persistable<UUID> {

    private static final long EXPIRED_OFFSET_DAY = 7;

    @Id
    private UUID id;

    private Long memberId;

    private LocalDateTime expiredAt;

    public RefreshToken(Long memberId, LocalDateTime expiredAt) {
        this.id = UUID.randomUUID();
        this.memberId = memberId;
        this.expiredAt = expiredAt;
    }

    public static RefreshToken of(Long memberId, LocalDateTime now) {
        return new RefreshToken(memberId, now.plusDays(EXPIRED_OFFSET_DAY));
    }

    public boolean isExpired(LocalDateTime now) {
        return expiredAt.isBefore(now);
    }

    @Nonnull
    public UUID getId() {
        return id;
    }

    @Override
    public boolean isNew() {
        return getCreatedAt() == null;
    }

    public Long getMemberId() {
        return memberId;
    }

    public LocalDateTime getExpiredAt() {
        return expiredAt;
    }
}
