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
            List<ArtistQueryModel> artistQueryModels = artists.stream()
                .map(ArtistQueryModel::from)
                .toList();
            return objectMapper.writeValueAsString(artistQueryModels);
        } catch (JsonProcessingException e) {
            log.error(e.getMessage(), e);
            throw new UnexpectedException("Artist 목록을 직렬화 하는 중에 문제가 발생했습니다.");
        }
    }

    /**
     * 쿼리에서 사용되는 모델이므로, 필드를 추가해도 필드명은 변경되면 절대로 안 됨!!!
     */
    public record ArtistQueryModel(
        Long id,
        String name,
        String profileImageUrl,
        String backgroundImageUrl
    ) {
        public static ArtistQueryModel from(Artist artist) {
            return new ArtistQueryModel(
                artist.getId(),
                artist.getName(),
                artist.getProfileImage(),
                artist.getBackgroundImageUrl()
            );
        }
    }
}
