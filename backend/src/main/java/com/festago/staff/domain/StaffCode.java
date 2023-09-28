package com.festago.staff.domain;

import com.festago.common.exception.ErrorCode;
import com.festago.common.exception.InternalServerException;
import com.festago.festival.domain.Festival;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.NotNull;

@Entity
public class StaffCode {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    @NotNull
    private StaffVerificationCode code;

    @OneToOne(fetch = FetchType.LAZY)
    private Festival festival;

    protected StaffCode() {
    }

    public StaffCode(StaffVerificationCode code, Festival festival) {
        this(null, code, festival);
    }

    public StaffCode(Long id, StaffVerificationCode code, Festival festival) {
        checkNotNull(code, festival);
        this.id = id;
        this.code = code;
        this.festival = festival;
    }

    private void checkNotNull(StaffVerificationCode code, Festival festival) {
        if (code == null || festival == null) {
            throw new InternalServerException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }

    public Long getId() {
        return id;
    }

    public StaffVerificationCode getCode() {
        return code;
    }

    public Festival getFestival() {
        return festival;
    }
}
