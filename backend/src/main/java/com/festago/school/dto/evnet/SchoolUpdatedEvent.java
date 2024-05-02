package com.festago.school.dto.evnet;

import com.festago.school.domain.School;

public record SchoolUpdatedEvent(
    School school
) {

}
