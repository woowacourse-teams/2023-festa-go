package com.festago.entry.dto;

import com.festago.entry.domain.EntryCode;

public record EntryCodeResponse(
    String code,
    Long period) {

    public static EntryCodeResponse of(EntryCode entryCode) {
        return new EntryCodeResponse(
            entryCode.getCode(),
            entryCode.getPeriod());
    }
}
