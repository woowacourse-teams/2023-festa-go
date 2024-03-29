package com.festago.mock.repository;

import com.festago.artist.domain.Artist;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ForMockArtistRepository extends JpaRepository<Artist, Long> {

}
