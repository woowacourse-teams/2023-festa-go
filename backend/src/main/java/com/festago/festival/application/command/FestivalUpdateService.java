package com.festago.festival.application.command;

import com.festago.festival.application.validator.FestivalUpdateValidator;
import com.festago.festival.domain.Festival;
import com.festago.festival.dto.command.FestivalUpdateCommand;
import com.festago.festival.repository.FestivalRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class FestivalUpdateService {

    private final FestivalRepository festivalRepository;
    private final List<FestivalUpdateValidator> validators;

    /**
     * 강제로 수정할 일이 필요할 수 있으므로, 시작일이 과거여도 예외를 발생하지 않음
     */
    public void updateFestival(Long festivalId, FestivalUpdateCommand command) {
        Festival festival = festivalRepository.getOrThrow(festivalId);
        /**
         * TODO
         * 일자를 수정할 때, 축제에 등록된 공연이 일자의 범위에 벗어난다면 문제가 발생하지 않을까?
         * StageFestivalUpdateValidator를 만들어서 검증이 필요함
         * 논의가 필요하므로 인터페이스만 만들고, 아직 구현하지 않음
         */
        validators.forEach(validator -> validator.validate(festivalId, command));
        festival.changeName(command.name());
        festival.changeThumbnail(command.thumbnail());
        festival.changeDate(command.startDate(), command.endDate());
    }
}
