package com.festago.stage.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
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
     * 역정규화를 위한 아티스트의 배열 JSON 컬럼<br/> [{ "id": 1, "name": "뉴진스", "imageUrl": "https://image.com/image.png" }]
     */
    @NotNull
    @Column(columnDefinition = "TEXT")
    private String artistInfo;

    private StageQueryInfo(Long stageId) {
        this.stageId = stageId;
        this.artistInfo = "[]";
    }

    public static StageQueryInfo create(Long stageId) {
        return new StageQueryInfo(stageId);
    }

    public void updateArtist(String artistInfo) {
        this.artistInfo = artistInfo;
    }

    public String getArtistInfo() {
        return artistInfo;
    }
}
