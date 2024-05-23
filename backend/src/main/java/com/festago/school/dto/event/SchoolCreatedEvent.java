package com.festago.school.dto.event;

import com.festago.school.domain.School;

public record SchoolCreatedEvent(
    School school
) {

}
