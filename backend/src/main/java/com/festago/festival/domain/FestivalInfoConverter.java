package com.festago.festival.domain;

import com.festago.artist.domain.Artist;
import java.util.List;

public interface FestivalInfoConverter {

    String convert(List<Artist> artists);

    List<Artist> convert(String value);
}
