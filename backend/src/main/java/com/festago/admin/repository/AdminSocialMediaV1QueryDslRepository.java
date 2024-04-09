package com.festago.admin.repository;

import static com.festago.socialmedia.domain.QSocialMedia.socialMedia;

import com.festago.admin.dto.socialmedia.AdminSocialMediaV1Response;
import com.festago.admin.dto.socialmedia.QAdminSocialMediaV1Response;
import com.festago.common.querydsl.QueryDslRepositorySupport;
import com.festago.socialmedia.domain.OwnerType;
import com.festago.socialmedia.domain.SocialMedia;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public class AdminSocialMediaV1QueryDslRepository extends QueryDslRepositorySupport {

    public AdminSocialMediaV1QueryDslRepository() {
        super(SocialMedia.class);
    }

    public Optional<AdminSocialMediaV1Response> findById(Long socialMediaId) {
        return fetchOne(query -> query.select(new QAdminSocialMediaV1Response(
                    socialMedia.id,
                    socialMedia.ownerId,
                    socialMedia.ownerType,
                    socialMedia.mediaType,
                    socialMedia.name,
                    socialMedia.logoUrl,
                    socialMedia.url
                ))
                .from(socialMedia)
                .where(socialMedia.id.eq(socialMediaId))
        );
    }

    // SocialMedia의 도메인 특성 상 SocialMediaType 개수만큼 row 생성이 제한되기에 limit을 사용하지 않음
    public List<AdminSocialMediaV1Response> findByOwnerIdAndOwnerType(Long ownerId, OwnerType ownerType) {
        return select(new QAdminSocialMediaV1Response(
            socialMedia.id,
            socialMedia.ownerId,
            socialMedia.ownerType,
            socialMedia.mediaType,
            socialMedia.name,
            socialMedia.logoUrl,
            socialMedia.url
        ))
            .from(socialMedia)
            .where(socialMedia.ownerId.eq(ownerId).and(socialMedia.ownerType.eq(ownerType)))
            .fetch();
    }
}
