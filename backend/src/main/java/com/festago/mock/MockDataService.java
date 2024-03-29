package com.festago.mock;

import com.festago.artist.application.ArtistCommandService;
import com.festago.artist.domain.Artist;
import com.festago.artist.dto.command.ArtistCreateCommand;
import com.festago.common.exception.ErrorCode;
import com.festago.common.exception.NotFoundException;
import com.festago.festival.application.command.FestivalCommandFacadeService;
import com.festago.festival.domain.Festival;
import com.festago.festival.dto.command.FestivalCreateCommand;
import com.festago.mock.repository.ForMockArtistRepository;
import com.festago.mock.repository.ForMockFestivalRepository;
import com.festago.mock.repository.ForMockSchoolRepository;
import com.festago.school.application.SchoolCommandService;
import com.festago.school.domain.School;
import com.festago.school.domain.SchoolRegion;
import com.festago.school.dto.SchoolCreateCommand;
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

    private static final AtomicLong festivalSequence = new AtomicLong();
    private static final int STAGE_START_HOUR = 19;
    private static final int STAGE_ARTIST_COUNT = 3;
    private static final int SCHOOL_PER_REGION = 3;
    private static final int DATE_OFFSET = 1;

    private final MockFestivalDateGenerator mockFestivalDateGenerator;
    private final ForMockSchoolRepository schoolRepository;
    private final ForMockArtistRepository artistRepository;
    private final ForMockFestivalRepository festivalRepository;
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

    /**
     * 각 지역 별로 3개의 학교를 만듭니다. ex) 서울1대학교 서울2대학교 서울3대학교
     */
    private void makeRegionSchools(SchoolRegion schoolRegion) {
        for (int i = 0; i < SCHOOL_PER_REGION; i++) {
            String schoolName = String.format("%s%d대학교", schoolRegion.name(), i + 1);
            String schoolEmail = String.format("%s%d.com", schoolRegion.name(), i + 1);
            crateSchool(schoolRegion, schoolName, schoolEmail);
        }
    }

    private void crateSchool(SchoolRegion schoolRegion, String schoolName, String schoolEmail) {
        schoolCommandService.createSchool(new SchoolCreateCommand(
                schoolName,
                schoolEmail,
                schoolRegion,
                null,
                null
            )
        );
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

    /**
     * 현재 날짜 + 입력받은 축제 기간 안의 기간을 갖는 축제를 생성합니다. 이때 하나의 축제에 중복된 아티스트가 포함되지 않기 위해서 makeRandomArtists 라는 메서드를 통해 섞인 Artist
     * 들의 큐가 생성됩니다.
     */
    private void makeFestival(int availableFestivalDuration, School school, List<Artist> artists) {
        LocalDate now = LocalDate.now();
        LocalDate startDate = mockFestivalDateGenerator.makeRandomStartDate(availableFestivalDuration, now);
        LocalDate endDate = mockFestivalDateGenerator.makeRandomEndDate(availableFestivalDuration, now, startDate);

        Long newFestivalId = festivalCommandFacadeService.createFestival(new FestivalCreateCommand(
            school.getName() + "축제" + festivalSequence.incrementAndGet(),
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

    /**
     * 축제 기간 동안 축제를 채웁니다. 에를 들어 Festival 이 23~25일 이라면 23, 24, 25 날짜의 stage 를 생성합니다.
     */
    private void makeStages(Long festivalId, Queue<Artist> artists) {
        Festival festival = festivalRepository.findById(festivalId)
            .orElseThrow(() -> new NotFoundException(ErrorCode.FESTIVAL_NOT_FOUND));
        LocalDate endDate = festival.getEndDate();
        LocalDate startDate = festival.getStartDate();
        startDate.datesUntil(endDate.plusDays(DATE_OFFSET))
            .forEach(localDate -> makeStage(festival, artists, localDate));
    }

    /**
     * 실질적으로 무대를 만드는 부분으로 이때 하나의 stage 는 랜덤한 아티스트 3명을 갖도록 만듭니다. 축제 별로 생성되는 queue 에서 poll 을 통해 stageArtist 를 결정하기 때문에 같은
     * 축제에서 아티스트는 중복되지 않습니다.
     */
    private void makeStage(Festival festival, Queue<Artist> artists, LocalDate localDate) {
        LocalDateTime startTime = localDate.atTime(STAGE_START_HOUR, 0);
        stageCommandFacadeService.createStage(new StageCreateCommand(
            festival.getId(),
            startTime,
            startTime.minusDays(1L),
            makeStageArtists(artists)
        ));
    }

    /**
     * 아티스트는 무대별로 3명 배치하였고 이는 임의로 설정된 값입니다.
     */
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
