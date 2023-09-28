package com.festago.staff.domain;

import com.festago.common.exception.ErrorCode;
import com.festago.common.exception.InternalServerException;
import com.festago.festival.domain.Festival;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
public class StaffCode {

    public static final int RANDOM_CODE_LENGTH = 4;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(max = 30)
    private String code;

    @OneToOne(fetch = FetchType.LAZY)
    private Festival festival;

    protected StaffCode() {
    }

    public StaffCode(String code, Festival festival) {
        this(null, code, festival);
    }

    public StaffCode(Long id, String code, Festival festival) {
        checkNotNull(code, festival);
        this.id = id;
        this.code = code;
        this.festival = festival;
    }

    private void checkNotNull(String code, Festival festival) {
        if (code == null || festival == null) {
            throw new InternalServerException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }

    public Long getId() {
        return id;
    }

    public String getCode() {
        return code;
    }

    public Festival getFestival() {
        return festival;
    }
}
