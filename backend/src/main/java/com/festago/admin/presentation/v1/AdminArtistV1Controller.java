package com.festago.admin.presentation.v1;

import com.festago.admin.application.AdminArtistV1QueryService;
import com.festago.admin.dto.artist.AdminArtistV1Response;
import com.festago.admin.dto.artist.ArtistV1CreateRequest;
import com.festago.admin.dto.artist.ArtistV1UpdateRequest;
import com.festago.artist.application.ArtistCommandService;
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
    private final AdminArtistV1QueryService artistV1QueryService;

    @PostMapping
    public ResponseEntity<Void> create(@RequestBody @Valid ArtistV1CreateRequest request) {
        Long artistId = artistCommandService.save(request.toCommand());
        return ResponseEntity.created(URI.create("/admin/api/v1/artists/" + artistId))
            .build();
    }

    @PutMapping("/{artistId}")
    public ResponseEntity<Void> update(
        @RequestBody @Valid ArtistV1UpdateRequest request,
        @PathVariable Long artistId
    ) {
        artistCommandService.update(request.toCommand(), artistId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{artistId}")
    public ResponseEntity<Void> delete(@PathVariable Long artistId) {
        artistCommandService.delete(artistId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{artistId}")
    public ResponseEntity<AdminArtistV1Response> findById(@PathVariable Long artistId) {
        AdminArtistV1Response response = artistV1QueryService.findById(artistId);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<AdminArtistV1Response>> findAll() {
        List<AdminArtistV1Response> response = artistV1QueryService.findAll();
        return ResponseEntity.ok(response);
    }
}

