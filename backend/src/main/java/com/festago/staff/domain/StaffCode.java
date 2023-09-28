package com.festago.staff.domain;

import com.festago.festival.domain.Festival;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class StaffCode {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private StaffVerificationCode code;

    @ManyToOne(fetch = FetchType.LAZY)
    private Festival festival;

    protected StaffCode() {
    }

    public StaffCode(StaffVerificationCode code, Festival festival) {
        this(null, code, festival);
    }

    public StaffCode(Long id, StaffVerificationCode code, Festival festival) {
        this.id = id;
        this.code = code;
        this.festival = festival;
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
