package com.festago.stage.domain;

import com.festago.artist.domain.Artist;
import com.festago.artist.domain.ArtistsSerializer;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class StageQueryInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private Long stageId;

    /**
     * 역정규화를 위한 아티스트의 배열 JSON 컬럼
     */
    @NotNull
    @Column(columnDefinition = "TEXT")
    private String artistInfo;

    private StageQueryInfo(Long stageId, String artistInfo) {
        this.stageId = stageId;
        this.artistInfo = artistInfo;
    }

    public static StageQueryInfo of(Long stageId, List<Artist> artists, ArtistsSerializer serializer) {
        return new StageQueryInfo(stageId, serializer.serialize(artists));
    }

    public void updateArtist(List<Artist> artists, ArtistsSerializer serializer) {
        this.artistInfo = serializer.serialize(artists);
    }

    public Long getId() {
        return id;
    }

    public Long getStageId() {
        return stageId;
    }

    public String getArtistInfo() {
        return artistInfo;
    }
}
