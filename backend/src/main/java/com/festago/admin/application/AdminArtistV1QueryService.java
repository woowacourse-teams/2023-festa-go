package com.festago.admin.application;

import com.festago.admin.dto.artist.AdminArtistV1Response;
import com.festago.artist.domain.Artist;
import com.festago.artist.repository.ArtistRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

// TODO 페이징 적용
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AdminArtistV1QueryService {

    private final ArtistRepository artistRepository;

    public AdminArtistV1Response findById(Long artistId) {
        Artist artist = artistRepository.getOrThrow(artistId);
        return AdminArtistV1Response.from(artist);
    }

    public List<AdminArtistV1Response> findAll() {
        return artistRepository.findAll().stream()
            .map(AdminArtistV1Response::from)
            .toList();
    }
}
