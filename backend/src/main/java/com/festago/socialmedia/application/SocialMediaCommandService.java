package com.festago.socialmedia.application;

import com.festago.artist.repository.ArtistRepository;
import com.festago.common.exception.BadRequestException;
import com.festago.common.exception.ErrorCode;
import com.festago.common.exception.NotFoundException;
import com.festago.school.repository.SchoolRepository;
import com.festago.socialmedia.domain.OwnerType;
import com.festago.socialmedia.domain.SocialMedia;
import com.festago.socialmedia.domain.SocialMediaType;
import com.festago.socialmedia.dto.command.SocialMediaCreateCommand;
import com.festago.socialmedia.dto.command.SocialMediaUpdateCommand;
import com.festago.socialmedia.repository.SocialMediaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class SocialMediaCommandService {

    private final SocialMediaRepository socialMediaRepository;
    private final SchoolRepository schoolRepository;
    private final ArtistRepository artistRepository;

    public Long createSocialMedia(SocialMediaCreateCommand command) {
        validate(command);
        SocialMedia socialMedia = socialMediaRepository.save(command.toEntity());
        return socialMedia.getId();
    }

    private void validate(SocialMediaCreateCommand command) {
        Long ownerId = command.ownerId();
        OwnerType ownerType = command.ownerType();
        SocialMediaType socialMediaType = command.socialMediaType();
        if (socialMediaRepository.existsByOwnerIdAndOwnerTypeAndMediaType(ownerId, ownerType, socialMediaType)) {
            throw new BadRequestException(ErrorCode.DUPLICATE_SOCIAL_MEDIA);
        }
        // TODO 추상적인 에러 코드가 필요할지? ex) ErrorCode.SOCIAL_MEDIA_OWNER_NOT_FOUND
        if (ownerType == OwnerType.ARTIST && !artistRepository.existsById(ownerId)) {
            throw new NotFoundException(ErrorCode.ARTIST_NOT_FOUND);
        }
        if (ownerType == OwnerType.SCHOOL && !schoolRepository.existsById(ownerId)) {
            throw new NotFoundException(ErrorCode.SCHOOL_NOT_FOUND);
        }
    }

    public void updateSocialMedia(Long socialMediaId, SocialMediaUpdateCommand command) {
        SocialMedia socialMedia = socialMediaRepository.getOrThrow(socialMediaId);
        socialMedia.changeName(command.name());
        socialMedia.changeLogoUrl(command.logoUrl());
        socialMedia.changeUrl(command.url());
    }
}
