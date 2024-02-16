package com.festago.artist.application;

import static com.festago.artist.domain.QArtist.artist;
import static com.festago.socialmedia.domain.QSocialMedia.socialMedia;
import static com.querydsl.core.group.GroupBy.groupBy;
import static com.querydsl.core.group.GroupBy.list;

import com.festago.artist.domain.Artist;
import com.festago.artist.dto.ArtistDetailV1Response;
import com.festago.artist.dto.QArtistDetailV1Response;
import com.festago.artist.dto.QArtistMediaV1Response;
import com.festago.common.querydsl.QueryDslRepositorySupport;
import com.festago.socialmedia.domain.OwnerType;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public class ArtistDetailV1QueryDslRepository extends QueryDslRepositorySupport {

    public ArtistDetailV1QueryDslRepository() {
        super(Artist.class);
    }

    public Optional<ArtistDetailV1Response> findArtistDetail(Long artistId) {
        List<ArtistDetailV1Response> response = selectFrom(artist)
            .leftJoin(socialMedia)
            .on(socialMedia.ownerId.eq(artist.id).and(socialMedia.ownerType.eq(OwnerType.ARTIST)))
            .where(artist.id.eq(artistId))
            .transform(
                groupBy(artist.id).list(
                    new QArtistDetailV1Response(
                        artist.id,
                        artist.name,
                        artist.profileImage,
                        artist.backgroundImageUrl,
                        list(new QArtistMediaV1Response(
                            socialMedia.mediaType.stringValue(),
                            socialMedia.name,
                            socialMedia.logoUrl,
                            socialMedia.url
                        ).skipNulls())
                    )
                )
            );

        if (response.isEmpty()) {
            return Optional.empty();
        }
        
        return Optional.of(response.get(0));
    }
}
