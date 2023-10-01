package com.festago.staff.dto;

import com.festago.staff.domain.Staff;

public record StaffResponse(
    Long id,
    String code
) {

    public static StaffResponse from(Staff staff) {
        return new StaffResponse(
            staff.getId(),
            staff.getCode().getValue()
        );
    }
}
