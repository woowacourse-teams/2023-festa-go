package com.festago.artist.repository;

import com.festago.artist.domain.Artist;
import org.springframework.data.repository.Repository;

public interface ArtistRepository extends Repository<Artist, Long> {

    Artist save(Artist artist);
}
