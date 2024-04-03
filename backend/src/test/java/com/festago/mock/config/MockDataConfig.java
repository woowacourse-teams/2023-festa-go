package com.festago.mock.config;

import com.festago.artist.application.ArtistCommandService;
import com.festago.artist.repository.ArtistRepository;
import com.festago.festival.application.command.FestivalCommandFacadeService;
import com.festago.festival.repository.FestivalRepository;
import com.festago.mock.CommandLineAppStartupRunner;
import com.festago.mock.MockScheduler;
import com.festago.mock.MockFestivalDateGenerator;
import com.festago.mock.MockDataService;
import com.festago.mock.RandomMockFestivalDateGenerator;
import com.festago.mock.repository.ForMockArtistRepository;
import com.festago.mock.repository.ForMockFestivalRepository;
import com.festago.mock.repository.ForMockSchoolRepository;
import com.festago.school.application.SchoolCommandService;
import com.festago.school.repository.SchoolRepository;
import com.festago.stage.application.command.StageCommandFacadeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class MockDataConfig {

    @Autowired
    private ForMockSchoolRepository schoolRepository;

    @Autowired
    private ForMockArtistRepository artistRepository;

    @Autowired
    private ForMockFestivalRepository festivalRepository;

    @Autowired
    private FestivalCommandFacadeService festivalCommandFacadeService;

    @Autowired
    private StageCommandFacadeService stageCommandFacadeService;

    @Autowired
    private ArtistCommandService artistCommandService;

    @Autowired
    private SchoolCommandService schoolCommandService;


    @Bean
    public MockFestivalDateGenerator festivalDateGenerator() {
        return new RandomMockFestivalDateGenerator();
    }

    @Bean
    public MockDataService mockDataService() {
        return new MockDataService(festivalDateGenerator(), schoolRepository, artistRepository, festivalRepository,
            festivalCommandFacadeService, stageCommandFacadeService, artistCommandService, schoolCommandService);
    }

    @Bean
    public MockScheduler mockScheduler() {
        return new MockScheduler(mockDataService());

    }

    @Bean
    public CommandLineAppStartupRunner commandLineAppStartupRunner() {
        return new CommandLineAppStartupRunner(mockDataService(), mockScheduler());
    }
}
