package com.festago.artist.repository;

import com.festago.artist.domain.Artist;
import com.festago.common.exception.ErrorCode;
import com.festago.common.exception.NotFoundException;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import org.springframework.data.repository.Repository;

public interface ArtistRepository extends Repository<Artist, Long> {

    default Artist getOrThrow(Long artistId) {
        return findById(artistId)
            .orElseThrow(() -> new NotFoundException(ErrorCode.ARTIST_NOT_FOUND));
    }

    Artist save(Artist artist);

    void deleteById(Long artistId);

    Optional<Artist> findById(Long id);

    long countByIdIn(List<Long> artistIds);

    List<Artist> findByIdIn(Collection<Long> artistIds);

    boolean existsById(Long id);
}
