package com.festago.admin.application;

import com.festago.admin.dto.socialmedia.AdminSocialMediaV1Response;
import com.festago.admin.repository.AdminSocialMediaV1QueryDslRepository;
import com.festago.common.exception.ErrorCode;
import com.festago.common.exception.NotFoundException;
import com.festago.socialmedia.domain.OwnerType;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AdminSocialMediaV1QueryService {

    private final AdminSocialMediaV1QueryDslRepository adminSocialMediaV1QueryDslRepository;

    public AdminSocialMediaV1Response findById(Long socialMediaId) {
        return adminSocialMediaV1QueryDslRepository.findById(socialMediaId)
            .orElseThrow(() -> new NotFoundException(ErrorCode.SOCIAL_MEDIA_NOT_FOUND));
    }

    public List<AdminSocialMediaV1Response> findByOwnerIdAndOwnerType(Long ownerId, OwnerType ownerType) {
        return adminSocialMediaV1QueryDslRepository.findByOwnerIdAndOwnerType(ownerId, ownerType);
    }
}
