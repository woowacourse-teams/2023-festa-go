package com.festago.mock.application;

import com.festago.artist.domain.Artist;
import com.festago.festival.domain.Festival;
import com.festago.festival.dto.event.FestivalCreatedEvent;
import com.festago.mock.domain.MockArtistsGenerator;
import com.festago.mock.domain.MockFestivalsGenerator;
import com.festago.mock.domain.MockSchoolsGenerator;
import com.festago.mock.domain.MockStageArtistsGenerator;
import com.festago.mock.domain.MockStagesGenerator;
import com.festago.mock.repository.ForMockArtistRepository;
import com.festago.mock.repository.ForMockFestivalRepository;
import com.festago.mock.repository.ForMockSchoolRepository;
import com.festago.mock.repository.ForMockStageArtistRepository;
import com.festago.mock.repository.ForMockStageRepository;
import com.festago.school.domain.School;
import com.festago.stage.domain.Stage;
import com.festago.stage.dto.event.StageCreatedEvent;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class MockDataService {

    private final MockArtistsGenerator mockArtistsGenerator;
    private final MockSchoolsGenerator mockSchoolsGenerator;
    private final MockFestivalsGenerator mockFestivalsGenerator;
    private final MockStagesGenerator mockStagesGenerator;
    private final MockStageArtistsGenerator mockStageArtistsGenerator;
    private final ForMockSchoolRepository schoolRepository;
    private final ForMockArtistRepository artistRepository;
    private final ForMockFestivalRepository festivalRepository;
    private final ForMockStageRepository stageRepository;
    private final ForMockStageArtistRepository stageArtistRepository;
    private final ApplicationEventPublisher eventPublisher;

    public void makeMockArtists() {
        artistRepository.saveAll(mockArtistsGenerator.generate());
    }

    public void makeMockSchools() {
        schoolRepository.saveAll(mockSchoolsGenerator.generate());
    }

    public void makeMockFestivals() {
        List<Artist> artists = artistRepository.findAll();
        List<School> schools = schoolRepository.findAll();
        List<Festival> festivals = festivalRepository.saveAll(
            mockFestivalsGenerator.generate(schools)
        );
        for (Festival festival : festivals) {
            List<Stage> stages = stageRepository.saveAll(mockStagesGenerator.generate(festival));
            stageArtistRepository.saveAll(mockStageArtistsGenerator.generate(stages, artists));
            eventPublisher.publishEvent(new FestivalCreatedEvent(festival));
            for (Stage stage : stages) {
                eventPublisher.publishEvent(new StageCreatedEvent(stage));
            }
        }
    }
}
