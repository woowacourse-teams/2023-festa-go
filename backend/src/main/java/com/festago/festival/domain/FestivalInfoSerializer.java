package com.festago.festival.domain;

import com.festago.artist.domain.Artist;
import java.util.List;

@Deprecated(forRemoval = true)
public interface FestivalInfoSerializer {

    String serialize(List<Artist> artists);
}
