package com.festago.dto;

import com.festago.domain.EntryCode;

public record EntryCodeResponse(String code, Long period) {

    public static EntryCodeResponse of(EntryCode entryCode) {
        return new EntryCodeResponse(entryCode.getCode(), entryCode.getPeriod());
    }
}
