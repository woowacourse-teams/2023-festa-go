package com.festago.mock;

import com.festago.artist.application.ArtistCommandService;
import com.festago.artist.domain.Artist;
import com.festago.artist.dto.command.ArtistCreateCommand;
import com.festago.artist.repository.ArtistRepository;
import com.festago.common.exception.ErrorCode;
import com.festago.common.exception.NotFoundException;
import com.festago.festival.application.command.FestivalCommandFacadeService;
import com.festago.festival.domain.Festival;
import com.festago.festival.dto.command.FestivalCreateCommand;
import com.festago.festival.repository.FestivalRepository;
import com.festago.school.application.SchoolCommandService;
import com.festago.school.domain.School;
import com.festago.school.domain.SchoolRegion;
import com.festago.school.dto.SchoolCreateCommand;
import com.festago.school.repository.SchoolRepository;
import com.festago.stage.application.command.StageCommandFacadeService;
import com.festago.stage.dto.command.StageCreateCommand;
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
    private final FestivalCommandFacadeService festivalCommandFacadeService;
    private final StageCommandFacadeService stageCommandFacadeService;
    private final ArtistCommandService artistCommandService;
    private final SchoolCommandService schoolCommandService;


    public boolean initialize() {
        if (alreadyInitialized()) {
            return false;
        }
        initializeData();
        return true;
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
            makeRegionSchools(schoolRegion);
        }
    }

    private void makeRegionSchools(SchoolRegion schoolRegion) {
        for (int i = 0; i < 3; i++) {
            int schoolNumber = i + 1;
            String schoolName = schoolRegion.name() + schoolNumber;
            schoolCommandService.createSchool(new SchoolCreateCommand(
                    schoolName,
                    schoolName + ".com",
                    schoolRegion,
                    null,
                    null
                )
            );
        }
    }

    private void initializeArtist() {
        for (MockArtist artist : MockArtist.values()) {
            artistCommandService.save(new ArtistCreateCommand(
                    artist.name(),
                    artist.getProfileImage(),
                    artist.getBackgroundImageUrl()
                )
            );
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

        Long newFestivalId = festivalCommandFacadeService.createFestival(new FestivalCreateCommand(
            school.getName() + "대 축제" + FESTIVAL_SEQUENCE.incrementAndGet(),
            startDate,
            endDate,
            "https://picsum.photos/536/354",
            school.getId()
        ));

        makeStages(newFestivalId, makeRandomArtists(artists));
    }

    private Queue<Artist> makeRandomArtists(List<Artist> artists) {
        List<Artist> randomArtists = new ArrayList<>(artists);
        Collections.shuffle(randomArtists);
        return new ArrayDeque<>(randomArtists);
    }

    private void makeStages(Long festivalId, Queue<Artist> artists) {
        Festival festival = festivalRepository.findById(festivalId)
            .orElseThrow(() -> new NotFoundException(ErrorCode.FESTIVAL_NOT_FOUND));
        LocalDate endDate = festival.getEndDate();
        LocalDate dateCursor = festival.getStartDate();
        while (dateCursor.isBefore(endDate) || dateCursor.equals(endDate)) {
            makeStage(festival, artists, dateCursor);
            dateCursor = dateCursor.plusDays(1);
        }
    }

    private void makeStage(Festival festival, Queue<Artist> artists, LocalDate localDate) {
        LocalDateTime startTime = localDate.atStartOfDay().plusHours(STAGE_START_HOUR);
        stageCommandFacadeService.createStage(new StageCreateCommand(
            festival.getId(),
            startTime,
            startTime.minusDays(1L),
            makeStageArtists(artists)
        ));
    }

    private List<Long> makeStageArtists(Queue<Artist> artists) {
        List<Artist> result = new ArrayList<>();
        for (int i = 0; i < STAGE_ARTIST_COUNT; i++) {
            Artist artist = artists.poll();
            if (artist == null) {
                throw new IllegalArgumentException("축제를 구성하기 위한 아티스트가 부족합니다");
            }
            result.add(artist);
        }
        return result.stream()
            .map(Artist::getId)
            .toList();
    }
}
