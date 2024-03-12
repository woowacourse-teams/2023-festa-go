package com.festago.admin.presentation.v1;

import com.festago.admin.application.AdminFestivalV1QueryService;
import com.festago.admin.dto.festival.AdminFestivalDetailV1Response;
import com.festago.admin.dto.festival.AdminFestivalV1Response;
import com.festago.admin.dto.festival.FestivalV1CreateRequest;
import com.festago.admin.dto.festival.FestivalV1UpdateRequest;
import com.festago.common.aop.ValidPageable;
import com.festago.common.querydsl.SearchCondition;
import com.festago.festival.application.command.FestivalCommandFacadeService;
import io.swagger.v3.oas.annotations.Hidden;
import jakarta.validation.Valid;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
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
@RequestMapping("/admin/api/v1/festivals")
@RequiredArgsConstructor
@Hidden
public class AdminFestivalV1Controller {

    private final AdminFestivalV1QueryService adminFestivalV1QueryService;
    private final FestivalCommandFacadeService festivalCommandFacadeService;

    @ValidPageable(maxSize = 50)
    @GetMapping
    public ResponseEntity<Page<AdminFestivalV1Response>> findAll(
        @RequestParam(defaultValue = "") String searchFilter,
        @RequestParam(defaultValue = "") String searchKeyword,
        @PageableDefault(size = 10) Pageable pageable
    ) {
        return ResponseEntity.ok()
            .body(adminFestivalV1QueryService.findAll(new SearchCondition(searchFilter, searchKeyword, pageable)));
    }

    @GetMapping("/{festivalId}")
    public ResponseEntity<AdminFestivalDetailV1Response> findDetail(
        @PathVariable Long festivalId
    ) {
        return ResponseEntity.ok()
            .body(adminFestivalV1QueryService.findDetail(festivalId));
    }

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
}
