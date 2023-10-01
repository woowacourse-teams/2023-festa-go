package com.festago.staff.domain;

import com.festago.common.exception.ErrorCode;
import com.festago.common.exception.InternalServerException;
import com.festago.festival.domain.Festival;
import com.festago.ticketing.domain.MemberTicket;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.NotNull;

@Entity
public class Staff {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    @NotNull
    private StaffCode code;

    @OneToOne(fetch = FetchType.LAZY)
    private Festival festival;

    protected Staff() {
    }

    public Staff(StaffCode code, Festival festival) {
        this(null, code, festival);
    }

    public Staff(Long id, StaffCode code, Festival festival) {
        checkNotNull(code, festival);
        this.id = id;
        this.code = code;
        this.festival = festival;
    }

    private void checkNotNull(StaffCode code, Festival festival) {
        if (code == null || festival == null) {
            throw new InternalServerException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }

    public boolean canValidate(MemberTicket memberTicket) {
        return memberTicket.belongsToFestival(festival.getId());
    }

    public Long getId() {
        return id;
    }

    public StaffCode getCode() {
        return code;
    }

    public Festival getFestival() {
        return festival;
    }
}
