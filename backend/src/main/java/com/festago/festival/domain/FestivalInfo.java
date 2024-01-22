package com.festago.festival.domain;

import com.festago.artist.domain.Artist;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FestivalInfo {

    @Id
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "festival_id")
    private Festival festival;

    @NotNull
    @Column(columnDefinition = "TEXT")
    private String artistInfo;

    private FestivalInfo(Festival festival, String artistInfo) {
        this.festival = festival;
        this.artistInfo = artistInfo;
    }

    public static FestivalInfo of(Festival festival, List<Artist> artists, FestivalInfoConverter converter) {
        return new FestivalInfo(festival, converter.convert(artists));
    }

    public Long getId() {
        return id;
    }

    public Festival getFestival() {
        return festival;
    }

    public String getArtistInfo() {
        return artistInfo;
    }
}
