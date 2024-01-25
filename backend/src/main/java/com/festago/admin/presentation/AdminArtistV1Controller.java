package com.festago.admin.presentation;

import com.festago.artist.application.ArtistCommandService;
import com.festago.artist.application.ArtistQueryService;
import com.festago.artist.dto.ArtistCreateRequest;
import com.festago.artist.dto.ArtistResponse;
import com.festago.artist.dto.ArtistUpdateRequest;
import io.swagger.v3.oas.annotations.Hidden;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/admin/api/v1/artists")
@RequiredArgsConstructor
@Hidden
public class AdminArtistV1Controller {

    private final ArtistCommandService artistCommandService;
    private final ArtistQueryService artistQueryService;

    @PostMapping
    public ResponseEntity<Void> create(@RequestBody @Valid ArtistCreateRequest request) {
        Long artistId = artistCommandService.save(request);
        return ResponseEntity.created(URI.create("/admin/api/v1/artists/" + artistId))
                .build();
    }

    @PutMapping("/{artistId}")
    public ResponseEntity<Void> update(@RequestBody @Valid ArtistUpdateRequest request,
                                       @PathVariable Long artistId) {
        artistCommandService.update(request, artistId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{artistId}")
    public ResponseEntity<Void> delete(@PathVariable Long artistId) {
        artistCommandService.delete(artistId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{artistId}")
    public ResponseEntity<ArtistResponse> findById(@PathVariable Long artistId) {
        ArtistResponse response = artistQueryService.findById(artistId);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<ArtistResponse>> findAll() {
        List<ArtistResponse> response = artistQueryService.findAll();
        return ResponseEntity.ok(response);
    }
}

