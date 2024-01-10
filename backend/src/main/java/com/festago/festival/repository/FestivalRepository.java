package com.festago.festival.repository;

import com.festago.festival.domain.Festival;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FestivalRepository extends JpaRepository<Festival, Long>, FestivalRepositoryCustom {

}
