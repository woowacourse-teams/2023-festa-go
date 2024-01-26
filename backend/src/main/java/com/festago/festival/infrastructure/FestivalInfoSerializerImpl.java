package com.festago.festival.infrastructure;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.festago.artist.domain.Artist;
import com.festago.common.exception.ErrorCode;
import com.festago.common.exception.InternalServerException;
import com.festago.festival.domain.FestivalInfoSerializer;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FestivalInfoSerializerImpl implements FestivalInfoSerializer {

    private final ObjectMapper objectMapper;

    @Override
    public String serialize(List<Artist> artists) throws InternalServerException {
        try {
            return objectMapper.writeValueAsString(artists);
        } catch (JsonProcessingException e) {
            throw new InternalServerException(ErrorCode.FESTIVAL_INFO_CONVERT_ERROR);
        }
    }
}
