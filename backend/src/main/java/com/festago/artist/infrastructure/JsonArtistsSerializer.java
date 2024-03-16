package com.festago.artist.infrastructure;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.festago.artist.domain.Artist;
import com.festago.artist.domain.ArtistsSerializer;
import com.festago.common.exception.UnexpectedException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class JsonArtistsSerializer implements ArtistsSerializer {

    private final ObjectMapper objectMapper;

    @Override
    public String serialize(List<Artist> artists) {
        try {
            return objectMapper.writeValueAsString(artists);
        } catch (JsonProcessingException e) {
            log.error(e.getMessage(), e);
            throw new UnexpectedException("Artist 목록을 직렬화 하는 중에 문제가 발생했습니다.");
        }
    }
}
