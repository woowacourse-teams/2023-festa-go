package com.festago.staff.dto;

import com.festago.staff.domain.StaffCode;

public record StaffCodeResponse(
    Long id,
    String code
) {

    public static StaffCodeResponse from(StaffCode staffCode) {
        return new StaffCodeResponse(
            staffCode.getId(),
            staffCode.getCode().getValue()
        );
    }
}
