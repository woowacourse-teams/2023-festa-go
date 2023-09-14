package com.festago.zfestival.repository;

import com.festago.zfestival.domain.Festival;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FestivalRepository extends JpaRepository<Festival, Long> {

}
