package com.festago.festival.repository;

import com.festago.common.exception.ErrorCode;
import com.festago.common.exception.NotFoundException;
import com.festago.festival.domain.Festival;
import java.util.Optional;
import org.springframework.data.repository.Repository;

public interface FestivalRepository extends Repository<Festival, Long>, FestivalRepositoryCustom {

    default Festival getOrThrow(Long festivalId) {
        return findById(festivalId)
            .orElseThrow(() -> new NotFoundException(ErrorCode.FESTIVAL_NOT_FOUND));
    }

    Festival save(Festival festival);

    Optional<Festival> findById(Long id);

    void deleteById(Long festivalId);

    void flush();
}
