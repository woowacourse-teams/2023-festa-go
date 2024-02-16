package com.festago.artist.presentation.v1;

import com.festago.artist.application.ArtistDetailV1QueryService;
import com.festago.artist.dto.ArtistDetailV1Response;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/artists")
@RequiredArgsConstructor
public class ArtistDetailV1Controller {

    private final ArtistDetailV1QueryService artistDetailV1QueryService;

    @GetMapping("/{artistId}")
    public ResponseEntity<ArtistDetailV1Response> getArtistInfo(@PathVariable Long artistId) {
        return ResponseEntity.ok(artistDetailV1QueryService.findArtistDetail(artistId));
    }
}
