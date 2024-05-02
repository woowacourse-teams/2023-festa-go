package com.festago.upload.domain;

import com.festago.common.exception.UnexpectedException;
import com.festago.common.util.Validator;
import jakarta.annotation.Nonnull;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import java.net.URI;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.domain.Persistable;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.util.MimeType;

@Slf4j
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class UploadFile implements Persistable<UUID> {

    @Id
    private UUID id;

    @Enumerated(EnumType.STRING)
    private UploadStatus status;

    private long size;

    @Convert(converter = URIStringConverter.class)
    private URI location;

    @Enumerated(EnumType.STRING)
    private FileExtension extension;

    private Long ownerId;

    @Enumerated(EnumType.STRING)
    private FileOwnerType ownerType;

    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    /**
     * UploadFile을 생성한다. <br/> UploadFile이 생성됐을 때, 파일은 어딘가에 업로드된 상태로 간주한다. <br/> 따라서 기본 상태는 UPLOADED 이다. <br/> 파일의 주인의
     * 생성 시점보다 파일이 더 먼저 생성될 수 있기에, ownerType, ownerId는 null이 될 수 있다. <br/>
     *
     * @param size      파일의 크기
     * @param location  파일이 저장된 URI
     * @param extension 파일의 확장자
     * @param createdAt 파일이 저장된 시간
     */
    public UploadFile(long size, URI location, FileExtension extension, LocalDateTime createdAt) {
        Validator.notNegative(size, "size");
        Validator.notNull(location, "location");
        Validator.notNull(extension, "extension");
        Validator.notNull(createdAt, "createdAt");
        this.id = UUID.randomUUID();
        this.status = UploadStatus.UPLOADED;
        this.size = size;
        this.location = location;
        this.extension = extension;
        this.ownerId = null;
        this.ownerType = null;
        this.createdAt = createdAt;
    }

    /**
     * 파일의 상태를 ASSIGNED로 변경한다. <br/> ASSIGNED 상태의 파일은 주인이 정해졌지만, 해당 주인이 파일을 소유하고 있지 않는 상태이다. <br/> 따라서 ASSIGNED 상태의 파일은
     * 같은 주인이라도 여러 개가 생길 수 있다. <br/> 이후 파일을 다른 상태로 변경하려면 renewalStatus() 또는 changeAbandoned()를 호출해야 한다. <br/>
     *
     * @param ownerId   파일 주인의 식별자
     * @param ownerType 파일 주인의 타입
     */
    public void changeAssigned(Long ownerId, FileOwnerType ownerType) {
        if (status != UploadStatus.UPLOADED) {
            throw new UnexpectedException("UPLOADED 상태의 파일만 ASSIGNED 상태로 변경할 수 있습니다. id=" + id);
        }
        this.status = UploadStatus.ASSIGNED;
        this.ownerId = ownerId;
        this.ownerType = ownerType;
    }

    /**
     * 파일의 상태를 ATTACHED로 변경한다. <br/> ATTACHED 상태의 파일은 주인이 해당 파일을 소유하고 있는 상태이다. <br/> 따라서 ATTACHED 상태의 파일은 주인이 가진 파일 개수를
     * 초과할 수 없다. <br/> 이후 파일을 다른 상태로 변경하려면 renewalStatus() 또는 changeAbandoned()를 호출해야 한다. <br/>
     *
     * @param ownerId   파일 주인의 식별자
     * @param ownerType 파일 주인의 타입
     */
    public void changeAttached(Long ownerId, FileOwnerType ownerType) {
        if (status != UploadStatus.UPLOADED) {
            throw new UnexpectedException("UPLOADED 상태의 파일만 ATTACHED 상태로 변경할 수 있습니다. id=" + id);
        }
        this.status = UploadStatus.ATTACHED;
        this.ownerId = ownerId;
        this.ownerType = ownerType;
    }

    /**
     * 현재 파일을 ABANDONED 상태로 변경한다. <br/> ABANDONED 상태의 파일은 더 이상 주인이 소유하고 있지 않는 것을 의미한다. <br/> 따라서 해당 파일은 다시 다른 상태로 변경할 수
     * 없고, 삭제 대상이 된다. <br/>
     */
    public void changeAbandoned() {
        status = UploadStatus.ABANDONED;
    }

    /**
     * ASSIGNED 또는 ATTACHED 상태의 파일을 ATTACHED 또는 ABANDONED 상태로 변경한다. <br/> 하지만 사용자가 파일 등록을 여러번 시도하여 ASSIGNED 상태의 파일이 다수
     * 생성될 수 있다. <br/> 따라서 최종적으로 등록된 파일만 ATTACHED 상태로 변경하고 나머지는 ABANDONED 상태로 변경해야 한다. <br/> 그렇기에 최종적으로 등록되야할 파일의 식별자
     * 목록을 받은 뒤, 식별자 목록에 현재 파일의 식별자가 있고, ASSIGNED 또는 ATTACHED 상태의 파일을 PRE_ATTACHED로 변경한다. <br/> 그 뒤, PRE_ATTACHED 상태가 되지
     * 못한 파일은 사용자가 최종적으로 등록한 파일이 아니므로 ABANDONED 상태로 변경한다. <br/> 그리고 PRE_ATTACHED 상태의 파일은 ATTACHED 상태로 변경한다. <br/>
     *
     * @param ids 최종적으로 ATTACHED 상태를 가져야 할 파일의 식별자 목록
     */
    public void renewalStatus(Set<UUID> ids) {
        if (ids.contains(id) && isAssignedOrAttached()) {
            status = UploadStatus.PRE_ATTACHED;
        }
        switch (status) {
            case PRE_ATTACHED -> status = UploadStatus.ATTACHED;
            case ASSIGNED, ATTACHED -> status = UploadStatus.ABANDONED;
            default -> {
                // NOOP
            }
        }
    }

    private boolean isAssignedOrAttached() {
        return status == UploadStatus.ASSIGNED || status == UploadStatus.ATTACHED;
    }

    public MimeType getMimeType() {
        return extension.getMimeType();
    }

    public URI getUploadUri() {
        return location.resolve(getName());
    }

    public String getName() {
        return createdAt.toLocalDate() + "/" + id + extension.getValue();
    }

    @Nonnull
    @Override
    public UUID getId() {
        return id;
    }

    @Override
    public boolean isNew() {
        return updatedAt == null;
    }

    public UploadStatus getStatus() {
        return status;
    }

    public long getSize() {
        return size;
    }

    public URI getLocation() {
        return location;
    }

    public FileExtension getExtension() {
        return extension;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public FileOwnerType getOwnerType() {
        return ownerType;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}
