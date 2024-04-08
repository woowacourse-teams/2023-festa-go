package com.festago.admin.presentation.v1;

import com.festago.admin.application.AdminSocialMediaV1QueryService;
import com.festago.admin.dto.socialmedia.AdminSocialMediaV1Response;
import com.festago.admin.dto.socialmedia.SocialMediaCreateV1Request;
import com.festago.admin.dto.socialmedia.SocialMediaUpdateV1Request;
import com.festago.socialmedia.application.SocialMediaCommandService;
import com.festago.socialmedia.domain.OwnerType;
import io.swagger.v3.oas.annotations.Hidden;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/api/v1/socialmedias")
@RequiredArgsConstructor
@Hidden
public class AdminSocialMediaV1Controller {

    private final SocialMediaCommandService socialMediaCommandService;
    private final AdminSocialMediaV1QueryService adminSocialMediaV1QueryService;

    @GetMapping
    public ResponseEntity<List<AdminSocialMediaV1Response>> findByOwnerIdAndOwnerType(
        @RequestParam Long ownerId,
        @RequestParam OwnerType ownerType
    ) {
        return ResponseEntity.ok()
            .body(adminSocialMediaV1QueryService.findByOwnerIdAndOwnerType(ownerId, ownerType));
    }

    @GetMapping("/{socialMediaId}")
    public ResponseEntity<AdminSocialMediaV1Response> findById(
        @PathVariable Long socialMediaId
    ) {
        return ResponseEntity.ok()
            .body(adminSocialMediaV1QueryService.findById(socialMediaId));
    }

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
