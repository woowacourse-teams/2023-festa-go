package com.festago.mock.config;

import com.festago.artist.domain.ArtistsSerializer;
import com.festago.artist.repository.ArtistRepository;
import com.festago.festival.repository.FestivalInfoRepository;
import com.festago.festival.repository.FestivalRepository;
import com.festago.mock.CommandLineAppStartupRunner;
import com.festago.mock.MockScheduler;
import com.festago.mock.application.FestivalDateGenerator;
import com.festago.mock.application.MockDataService;
import com.festago.mock.application.RandomFestivalDateGenerator;
import com.festago.school.repository.SchoolRepository;
import com.festago.stage.repository.StageArtistRepository;
import com.festago.stage.repository.StageQueryInfoRepository;
import com.festago.stage.repository.StageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class MockDataConfig {

    @Autowired
    private SchoolRepository schoolRepository;

    @Autowired
    private ArtistRepository artistRepository;

    @Autowired
    private FestivalRepository festivalRepository;

    @Autowired
    private StageRepository stageRepository;

    @Autowired
    private StageArtistRepository stageArtistRepository;

    @Autowired
    private FestivalInfoRepository festivalInfoRepository;

    @Autowired
    private StageQueryInfoRepository stageQueryInfoRepository;

    @Autowired
    private ArtistsSerializer artistsSerializer;


    @Bean
    public FestivalDateGenerator festivalDateGenerator() {
        return new RandomFestivalDateGenerator();
    }

    @Bean
    public MockDataService mockDataService() {
        return new MockDataService(festivalDateGenerator(), schoolRepository, artistRepository, festivalRepository,
            stageRepository, stageArtistRepository, festivalInfoRepository, stageQueryInfoRepository,
            artistsSerializer);
    }

    @Bean
    public MockScheduler mockScheduler(){
        return new MockScheduler(mockDataService());

    }
    @Bean
    public CommandLineAppStartupRunner commandLineAppStartupRunner(){
        return new CommandLineAppStartupRunner(mockDataService(), mockScheduler());
    }
}
