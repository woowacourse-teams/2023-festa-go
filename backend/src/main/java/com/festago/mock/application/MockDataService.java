package com.festago.mock.application;

import com.festago.artist.domain.Artist;
import com.festago.artist.domain.ArtistsSerializer;
import com.festago.artist.repository.ArtistRepository;
import com.festago.festival.domain.Festival;
import com.festago.festival.domain.FestivalQueryInfo;
import com.festago.festival.repository.FestivalInfoRepository;
import com.festago.festival.repository.FestivalRepository;
import com.festago.mock.MockArtist;
import com.festago.school.domain.School;
import com.festago.school.domain.SchoolRegion;
import com.festago.school.repository.SchoolRepository;
import com.festago.stage.domain.Stage;
import com.festago.stage.domain.StageArtist;
import com.festago.stage.domain.StageQueryInfo;
import com.festago.stage.repository.StageArtistRepository;
import com.festago.stage.repository.StageQueryInfoRepository;
import com.festago.stage.repository.StageRepository;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicLong;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Profile({"dev"})
@Service
@Transactional
@RequiredArgsConstructor
public class MockDataService {

    private static final AtomicLong FESTIVAL_SEQUENCE = new AtomicLong();
    private static final long STAGE_START_HOUR = 19L;
    private static final int STAGE_ARTIST_COUNT = 3;

    private final FestivalDateGenerator festivalDateGenerator;
    private final SchoolRepository schoolRepository;
    private final ArtistRepository artistRepository;
    private final FestivalRepository festivalRepository;
    private final StageRepository stageRepository;
    private final StageArtistRepository stageArtistRepository;
    private final FestivalInfoRepository festivalInfoRepository;
    private final StageQueryInfoRepository stageQueryInfoRepository;
    private final ArtistsSerializer artistsSerializer;

    public void initialize() {
        if (alreadyInitialized()) {
            return;
        }
        initializeData();
    }

    private boolean alreadyInitialized() {
        return !schoolRepository.findAll().isEmpty();
    }

    private void initializeData() {
        initializeSchool();
        initializeArtist();
    }

    private void initializeSchool() {
        for (SchoolRegion schoolRegion : SchoolRegion.values()) {
            if (SchoolRegion.ANY.equals(schoolRegion)) {
                continue;
            }
            schoolRepository.saveAll(makeRegionSchools(schoolRegion));
        }
    }

    private List<School> makeRegionSchools(SchoolRegion schoolRegion) {
        List<School> result = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            int schoolNumber = i + 1;
            String schoolName = schoolRegion.name() + schoolNumber;
            result.add(new School(
                schoolName + ".com",
                schoolName,
                schoolRegion
            ));
        }
        return result;
    }

    private void initializeArtist() {
        for (MockArtist artist : MockArtist.values()) {
            artistRepository.save(new Artist(artist.name(), artist.getProfileImage(), artist.getBackgroundImageUrl()));
        }
    }

    public void makeMockFestivals(int availableFestivalDuration) {
        List<School> allSchool = schoolRepository.findAll();
        List<Artist> allArtist = artistRepository.findAll();
        for (School school : allSchool) {
            makeFestival(availableFestivalDuration, school, allArtist);
        }
    }

    private void makeFestival(int availableFestivalDuration, School school, List<Artist> artists) {
        LocalDate now = LocalDate.now();
        LocalDate startDate = festivalDateGenerator.makeRandomStartDate(availableFestivalDuration, now);
        LocalDate endDate = festivalDateGenerator.makeRandomEndDate(availableFestivalDuration, now, startDate);

        Festival newFestival = festivalRepository.save(
            new Festival(school.getName() + " 축제" + FESTIVAL_SEQUENCE.incrementAndGet(),
                startDate,
                endDate,
                school));
        List<Artist> participatedArtists = makeStages(newFestival, makeRandomArtists(artists));

        makeFestivalQueryInfo(newFestival, participatedArtists);
    }

    private Queue<Artist> makeRandomArtists(List<Artist> artists) {
        List<Artist> randomArtists = new ArrayList<>(artists);
        Collections.shuffle(randomArtists);
        return new ArrayDeque<>(randomArtists);
    }

    private List<Artist> makeStages(Festival festival, Queue<Artist> artists) {
        LocalDate endDate = festival.getEndDate();
        LocalDate dateCursor = festival.getStartDate();
        List<Artist> participatedArtists = new ArrayList<>();
        while (dateCursor.isAfter(endDate)) {
            participatedArtists.addAll(makeStage(festival, artists, dateCursor));
            dateCursor = dateCursor.plusDays(1);
        }
        return participatedArtists;
    }

    private List<Artist> makeStage(Festival festival, Queue<Artist> artists, LocalDate localDate) {
        LocalDateTime startTime = localDate.atStartOfDay().plusHours(STAGE_START_HOUR);
        Stage stage = stageRepository.save(new Stage(startTime, startTime.minusDays(1L), festival));
        List<Artist> stageArtists = selectArtists(artists, STAGE_ARTIST_COUNT);
        makeStageArtists(stageArtists, stage);
        return stageArtists;
    }

    private List<Artist> selectArtists(Queue<Artist> artists, int count) {
        List<Artist> result = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            Artist artist = artists.poll();
            if (artist == null) {
                throw new IllegalArgumentException("축제를 만들기 위한 Artist 가 부족합니다");
            }
            result.add(artist);
        }
        return result;
    }

    private void makeStageArtists(List<Artist> artists, Stage stage) {
        for (Artist artist : artists) {
            stageArtistRepository.save(new StageArtist(stage.getId(), artist.getId()));
        }
        stageQueryInfoRepository.save(StageQueryInfo.of(stage.getId(), artists, artistsSerializer));
    }

    private void makeFestivalQueryInfo(Festival newFestival, List<Artist> participatedArtists) {
        FestivalQueryInfo festivalQueryInfo = FestivalQueryInfo.create(newFestival.getId());
        festivalQueryInfo.updateArtistInfo(participatedArtists, artistsSerializer);
        festivalInfoRepository.save(festivalQueryInfo);
    }
}
