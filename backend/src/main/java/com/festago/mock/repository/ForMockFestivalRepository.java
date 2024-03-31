package com.festago.mock.repository;

import com.festago.festival.domain.Festival;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ForMockFestivalRepository extends JpaRepository<Festival, Long> {

}
