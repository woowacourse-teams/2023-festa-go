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

    @Deprecated(forRemoval = true)
    private StageQueryInfo(Long stageId) {
        this.stageId = stageId;
        this.artistInfo = "[]";
    }

    private StageQueryInfo(Long stageId, String artistInfo) {
        this.stageId = stageId;
        this.artistInfo = artistInfo;
    }

    @Deprecated(forRemoval = true)
    public static StageQueryInfo create(Long stageId) {
        return new StageQueryInfo(stageId);
    }

    public static StageQueryInfo of(Long stageId, List<Artist> artists, ArtistsSerializer serializer) {
        return new StageQueryInfo(stageId, serializer.serialize(artists));
    }

    @Deprecated(forRemoval = true)
    public void updateArtist(String artistInfo) {
        this.artistInfo = artistInfo;
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
