package com.festago.festival.dto.command;

import com.festago.festival.domain.Festival;
import com.festago.school.domain.School;
import java.time.LocalDate;

public record FestivalCreateCommand(
    String name,
    LocalDate startDate,
    LocalDate endDate,
    String thumbnail,
    Long schoolId
) {

    public Festival toEntity(School school) {
        return new Festival(name, startDate, endDate, thumbnail, school);
    }
}
