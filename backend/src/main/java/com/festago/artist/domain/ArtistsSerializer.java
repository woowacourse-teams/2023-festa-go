package com.festago.artist.domain;

import java.util.List;

public interface ArtistsSerializer {

    String serialize(List<Artist> artists);
}
