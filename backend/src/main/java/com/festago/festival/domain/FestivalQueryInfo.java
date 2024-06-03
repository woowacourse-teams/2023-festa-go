package com.festago.festival.domain;

import static java.util.Comparator.comparingLong;

import com.festago.artist.domain.Artist;
import com.festago.artist.domain.ArtistsSerializer;
import com.festago.common.domain.BaseTimeEntity;
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
public class FestivalQueryInfo extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(unique = true)
    private Long festivalId;

    @NotNull
    @Column(columnDefinition = "TEXT")
    private String artistInfo;

    private FestivalQueryInfo(Long festivalId, String artistInfo) {
        this.festivalId = festivalId;
        this.artistInfo = artistInfo;
    }

    public static FestivalQueryInfo create(Long festivalId) {
        return new FestivalQueryInfo(festivalId, "[]");
    }

    public void updateArtistInfo(List<Artist> artists, ArtistsSerializer serializer) {
        List<Artist> distinctSortedArtists = artists.stream()
            .distinct()
            .sorted(comparingLong(Artist::getId))
            .toList();
        this.artistInfo = serializer.serialize(distinctSortedArtists);
    }

    public Long getId() {
        return id;
    }

    public Long getFestivalId() {
        return festivalId;
    }

    public String getArtistInfo() {
        return artistInfo;
    }
}
