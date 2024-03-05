package com.festago.mock.application;

import com.festago.artist.domain.Artist;
import com.festago.artist.repository.ArtistRepository;
import com.festago.mock.MockArtist;
import com.festago.school.domain.School;
import com.festago.school.domain.SchoolRegion;
import com.festago.school.repository.SchoolRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class MockDataService {

    private final SchoolRepository schoolRepository;
    private final ArtistRepository artistRepository;

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
            schoolRepository.save(new School("domain", schoolRegion.name() + "대 축제", schoolRegion));
        }
    }

    private void initializeArtist() {
        for (MockArtist artist : MockArtist.values()) {
            artistRepository.save(new Artist(artist.name(), artist.getProfileImage(), artist.getBackgroundImageUrl()));
        }
    }

    public void makeMockFestivals() {

    }
}
