package com.festago.stage.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class StageArtist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long stageId;
    private Long artistId;

    public StageArtist(Long stageId, Long artistId) {
        this(null, stageId, artistId);
    }

    public StageArtist(Long id, Long stageId, Long artistId) {
        this.id = id;
        this.stageId = stageId;
        this.artistId = artistId;
    }

    public Long getId() {
        return id;
    }

    public Long getStageId() {
        return stageId;
    }

    public Long getArtistId() {
        return artistId;
    }
}
