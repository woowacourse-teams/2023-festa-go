package com.festago.school.presentation.v1.dto;

import java.util.List;

public record SchoolsV1Response(
    List<SchoolV1Response> schools
) {

}
