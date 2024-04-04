package com.festago.admin.repository;

import static com.festago.artist.domain.QArtist.artist;

import com.festago.admin.dto.artist.AdminArtistV1Response;
import com.festago.admin.dto.artist.QAdminArtistV1Response;
import com.festago.artist.domain.Artist;
import com.festago.common.querydsl.QueryDslRepositorySupport;
import com.festago.common.querydsl.SearchCondition;
import com.querydsl.core.types.dsl.BooleanExpression;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

@Repository
public class AdminArtistV1QueryDslRepository extends QueryDslRepositorySupport {

    public AdminArtistV1QueryDslRepository() {
        super(Artist.class);
    }

    public Page<AdminArtistV1Response> findAll(SearchCondition searchCondition) {
        Pageable pageable = searchCondition.pageable();
        String searchFilter = searchCondition.searchFilter();
        String searchKeyword = searchCondition.searchKeyword();
        return applyPagination(pageable,
            queryFactory -> queryFactory.select(
                    new QAdminArtistV1Response(
                        artist.id,
                        artist.name,
                        artist.profileImage,
                        artist.backgroundImageUrl
                    ))
                .from(artist)
                .where(containSearchFilter(searchFilter, searchKeyword))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize()),
            queryFactory -> queryFactory.select(artist.count())
                .where(containSearchFilter(searchFilter, searchKeyword))
                .from(artist));
    }

    private BooleanExpression containSearchFilter(String searchFilter, String searchKeyword) {
        return switch (searchFilter) {
            case "id" -> eqId(searchKeyword);
            case "name" -> {
                // avoid NPE
                if (searchKeyword != null && searchKeyword.length() == 1) {
                    yield eqName(searchKeyword);
                }
                yield containsName(searchKeyword);
            }
            default -> null;
        };
    }

    private BooleanExpression eqId(String id) {
        if (StringUtils.hasText(id)) {
            return artist.id.stringValue().eq(id);
        }
        return null;
    }

    private BooleanExpression containsName(String name) {
        if (StringUtils.hasText(name)) {
            return artist.name.contains(name);
        }
        return null;
    }

    private BooleanExpression eqName(String name) {
        if (StringUtils.hasText(name)) {
            return artist.name.eq(name);
        }
        return null;
    }

    public Optional<AdminArtistV1Response> findById(Long artistId) {
        return fetchOne(queryFactory -> queryFactory.select(
                new QAdminArtistV1Response(
                    artist.id,
                    artist.name,
                    artist.profileImage,
                    artist.backgroundImageUrl
                ))
            .from(artist)
            .where(artist.id.eq(artistId)));
    }
}
