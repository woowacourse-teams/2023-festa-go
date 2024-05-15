package com.festago.artist.infrastructure;

import static java.util.stream.Collectors.joining;

import com.festago.artist.domain.Artist;
import com.festago.artist.domain.ArtistsSerializer;
import java.util.List;

public class DelimiterArtistsSerializer implements ArtistsSerializer {

    private final String delimiter;

    public DelimiterArtistsSerializer(String delimiter) {
        this.delimiter = delimiter;
    }

    @Override
    public String serialize(List<Artist> artists) {
        return artists.stream()
            .map(Artist::getName)
            .collect(joining(delimiter));
    }
}
