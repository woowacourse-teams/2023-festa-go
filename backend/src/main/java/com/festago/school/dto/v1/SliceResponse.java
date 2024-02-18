package com.festago.school.dto.v1;

import java.util.List;

public record SliceResponse<T>(
    Boolean last,
    List<T> content
) {

}
