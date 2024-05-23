package com.festago.artist.domain;

import java.util.List;

@FunctionalInterface
public interface ArtistsSerializer {

    String serialize(List<Artist> artists);
}
