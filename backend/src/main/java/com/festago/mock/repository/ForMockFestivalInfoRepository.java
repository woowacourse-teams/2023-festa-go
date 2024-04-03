package com.festago.mock.repository;

import com.festago.festival.domain.FestivalQueryInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ForMockFestivalInfoRepository extends JpaRepository<FestivalQueryInfo, Long> {

}
