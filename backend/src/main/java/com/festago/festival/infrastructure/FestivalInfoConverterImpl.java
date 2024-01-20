package com.festago.festival.infrastructure;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.festago.artist.domain.Artist;
import com.festago.common.exception.ErrorCode;
import com.festago.common.exception.InternalServerException;
import com.festago.festival.domain.FestivalInfoConverter;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FestivalInfoConverterImpl implements FestivalInfoConverter {

    private final ObjectMapper objectMapper;

    @Override
    public String convert(List<Artist> artists) throws InternalServerException {
        try {
            return objectMapper.writeValueAsString(artists);
        } catch (JsonProcessingException e) {
            throw new InternalServerException(ErrorCode.FESTIVAL_INFO_CONVERT_ERROR);
        }
    }

    @Override
    public List<Artist> convert(String value) {
        try {
            return objectMapper.readValue(value, new TypeReference<>() {
            });
        } catch (JsonProcessingException e) {
            throw new InternalServerException(ErrorCode.FESTIVAL_INFO_CONVERT_ERROR);
        }
    }
}
