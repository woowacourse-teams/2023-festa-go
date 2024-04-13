package com.festago.mock.domain;

import com.festago.artist.domain.Artist;
import java.util.Arrays;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class MockArtistsGenerator {

    public List<Artist> generate() {
        return Arrays.stream(MockArtist.values())
            .map(this::createArtist)
            .toList();
    }

    private Artist createArtist(MockArtist mockArtist) {
        return new Artist(mockArtist.name(), mockArtist.getProfileImage(), mockArtist.getBackgroundImageUrl());
    }
}
