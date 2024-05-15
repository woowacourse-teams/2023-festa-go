package com.festago.school.dto.event;

import com.festago.school.domain.School;

public record SchoolUpdatedEvent(
    School school
) {

}
