package com.festago.admin.presentation.v1;

import com.festago.admin.dto.socialmedia.SocialMediaCreateV1Request;
import com.festago.admin.dto.socialmedia.SocialMediaUpdateV1Request;
import com.festago.socialmedia.application.SocialMediaCommandService;
import io.swagger.v3.oas.annotations.Hidden;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/api/v1/socialmedias")
@RequiredArgsConstructor
@Hidden
public class AdminSocialMediaV1Controller {

    private final SocialMediaCommandService socialMediaCommandService;

    @PostMapping
    public ResponseEntity<Void> createSocialMedia(
        @RequestBody @Valid SocialMediaCreateV1Request request
    ) {
        socialMediaCommandService.createSocialMedia(request.toCommand());
        return ResponseEntity.ok()
            .build();
    }

    @PatchMapping("/{socialMediaId}")
    public ResponseEntity<Void> updateSocialMedia(
        @PathVariable Long socialMediaId,
        @RequestBody @Valid SocialMediaUpdateV1Request request
    ) {
        socialMediaCommandService.updateSocialMedia(socialMediaId, request.toCommand());
        return ResponseEntity.ok()
            .build();
    }

    @DeleteMapping("/{socialMediaId}")
    public ResponseEntity<Void> deleteSocialMedia(
        @PathVariable Long socialMediaId
    ) {
        socialMediaCommandService.deleteSocialMedia(socialMediaId);
        return ResponseEntity.noContent().build();
    }
}
