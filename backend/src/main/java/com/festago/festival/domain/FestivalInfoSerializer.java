package com.festago.festival.domain;

import com.festago.artist.domain.Artist;
import java.util.List;

public interface FestivalInfoSerializer {

    String serialize(List<Artist> artists);

    List<Artist> deserialize(String value);
}
