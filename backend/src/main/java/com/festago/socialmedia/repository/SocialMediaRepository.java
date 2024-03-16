package com.festago.socialmedia.repository;

import com.festago.socialmedia.domain.SocialMedia;
import org.springframework.data.repository.Repository;

public interface SocialMediaRepository extends Repository<SocialMedia, Long> {

    SocialMedia save(SocialMedia socialMedia);
}
