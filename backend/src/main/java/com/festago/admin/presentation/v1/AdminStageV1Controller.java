package com.festago.admin.presentation.v1;

import com.festago.admin.presentation.v1.dto.StageV1CreateRequest;
import com.festago.admin.presentation.v1.dto.StageV1UpdateRequest;
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
@RequestMapping("/admin/api/v1/stages")
@RequiredArgsConstructor
@Hidden
public class AdminStageV1Controller {

    private final StageCommandFacadeService stageCommandFacadeService;

    @PostMapping
    public ResponseEntity<Void> createStage(
        @RequestBody @Valid StageV1CreateRequest request
    ) {
        Long stageId = stageCommandFacadeService.createStage(request.toCommand());
        return ResponseEntity.created(URI.create("/admin/api/v1/stages/" + stageId))
            .build();
    }

    @PatchMapping("/{stageId}")
    public ResponseEntity<Void> updateStage(
        @PathVariable Long stageId,
        @RequestBody @Valid StageV1UpdateRequest request
    ) {
        stageCommandFacadeService.updateStage(stageId, request.toCommand());
        return ResponseEntity.ok()
            .build();
    }

    @DeleteMapping("/{stageId}")
    public ResponseEntity<Void> deleteStage(
        @PathVariable Long stageId
    ) {
        stageCommandFacadeService.deleteStage(stageId);
        return ResponseEntity.noContent()
            .build();
    }
}
