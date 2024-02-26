package com.festago.admin.presentation.v1;

import com.festago.admin.dto.FestivalV1CreateRequest;
import com.festago.admin.dto.FestivalV1UpdateRequest;
import com.festago.admin.presentation.v1.dto.StageV1CreateRequest;
import com.festago.admin.presentation.v1.dto.StageV1UpdateRequest;
import com.festago.festival.application.command.FestivalCommandFacadeService;
import com.festago.stage.application.command.StageCommandFacadeService;
import io.swagger.v3.oas.annotations.Hidden;
import jakarta.validation.Valid;
import java.net.URI;
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
@RequestMapping("/admin/api/v1/festivals")
@RequiredArgsConstructor
@Hidden
public class AdminFestivalV1Controller {

    private final FestivalCommandFacadeService festivalCommandFacadeService;
    private final StageCommandFacadeService stageCommandFacadeService;

    @PostMapping
    public ResponseEntity<Void> createFestival(
        @RequestBody @Valid FestivalV1CreateRequest request
    ) {
        Long festivalId = festivalCommandFacadeService.createFestival(request.toCommand());
        return ResponseEntity.created(URI.create("/admin/api/v1/festivals/" + festivalId))
            .build();
    }

    @PatchMapping("/{festivalId}")
    public ResponseEntity<Void> updateFestival(
        @PathVariable Long festivalId,
        @RequestBody @Valid FestivalV1UpdateRequest request
    ) {
        festivalCommandFacadeService.updateFestival(festivalId, request.toCommand());
        return ResponseEntity.ok()
            .build();
    }

    @DeleteMapping("/{festivalId}")
    public ResponseEntity<Void> deleteFestival(
        @PathVariable Long festivalId
    ) {
        festivalCommandFacadeService.deleteFestival(festivalId);
        return ResponseEntity.noContent()
            .build();
    }

    // TODO REST API 설계를 위해 FestivalController에 Stage 기능을 넣었는데 과연..?
    // Stage Controller가 생긴다면 경로를 어떻게 해야하지?
    @PostMapping("/{festivalId}/stages")
    public ResponseEntity<Void> createStage(
        @PathVariable Long festivalId,
        @RequestBody @Valid StageV1CreateRequest request
    ) {
        Long stageId = stageCommandFacadeService.createStage(request.toCommand(festivalId));
        return ResponseEntity.created(URI.create("/admin/api/v1/festivals/" + festivalId + "/stages/" + stageId))
            .build();
    }

    @PatchMapping("/{festivalId}/stages/{stageId}")
    public ResponseEntity<Void> updateStage(
        @PathVariable Long festivalId,
        @PathVariable Long stageId,
        @RequestBody @Valid StageV1UpdateRequest request
    ) {
        stageCommandFacadeService.updateStage(stageId, request.toCommand());
        return ResponseEntity.ok()
            .build();
    }

    @DeleteMapping("/{festivalId}/stages/{stageId}")
    public ResponseEntity<Void> deleteStage(
        @PathVariable Long festivalId,
        @PathVariable Long stageId
    ) {
        stageCommandFacadeService.deleteStage(stageId);
        return ResponseEntity.noContent()
            .build();
    }
}
