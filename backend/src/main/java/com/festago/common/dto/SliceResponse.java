package com.festago.common.dto;

import java.util.List;
import org.springframework.data.domain.Slice;

public record SliceResponse<T>(
    boolean last,
    List<T> content
) {

    public static <T> SliceResponse<T> from(Slice<T> slice) {
        return new SliceResponse(slice.isLast(), slice.getContent());
    }
}
