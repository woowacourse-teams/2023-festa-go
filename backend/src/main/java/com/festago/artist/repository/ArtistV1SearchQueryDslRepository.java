package com.festago.artist.repository;


import static com.festago.artist.domain.QArtist.artist;

import com.festago.artist.domain.Artist;
import com.festago.artist.dto.ArtistSearchResponse;
import com.festago.artist.dto.QArtistSearchResponse;
import com.festago.common.querydsl.QueryDslRepositorySupport;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public class ArtistV1SearchQueryDslRepository extends QueryDslRepositorySupport {

    public ArtistV1SearchQueryDslRepository() {
        super(Artist.class);
    }

    public List<ArtistSearchResponse> findAllByLike(String keyword) {
        return select(
            new QArtistSearchResponse(artist.id, artist.name, artist.profileImage))
            .from(artist)
            .where(artist.name.contains(keyword))
            .fetch();
    }

    public List<ArtistSearchResponse> findAllByEqual(String keyword) {
        return select(
            new QArtistSearchResponse(artist.id, artist.name, artist.profileImage))
            .from(artist)
            .where(artist.name.eq(keyword))
            .fetch();
    }
}
