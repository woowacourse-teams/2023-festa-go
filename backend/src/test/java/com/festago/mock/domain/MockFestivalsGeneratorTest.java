package com.festago.mock.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.mock;
import static org.mockito.BDDMockito.spy;

import com.festago.festival.domain.Festival;
import com.festago.mock.MockFestivalDateGenerator;
import com.festago.school.domain.School;
import com.festago.support.TimeInstantProvider;
import com.festago.support.fixture.SchoolFixture;
import java.time.Clock;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.IntStream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class MockFestivalsGeneratorTest {

    Clock clock;
    MockFestivalDateGenerator mockFestivalDateGenerator;
    MockFestivalsGenerator mockFestivalsGenerator;

    @BeforeEach
    void setUp() {
        clock = spy(Clock.systemDefaultZone());
        mockFestivalDateGenerator = mock(MockFestivalDateGenerator.class);
        mockFestivalsGenerator = new MockFestivalsGenerator(clock, mockFestivalDateGenerator);
    }

    @Test
    void 인자로_들어온_학교의_개수만큼_축제를_생성한다() {
        // given
        LocalDate now = LocalDate.parse("2077-06-30");
        given(clock.instant())
            .willReturn(TimeInstantProvider.from(now));
        given(mockFestivalDateGenerator.makeRandomStartDate(anyInt(), any(LocalDate.class)))
            .willReturn(now);
        given(mockFestivalDateGenerator.makeRandomEndDate(anyInt(), any(LocalDate.class), any(LocalDate.class)))
            .willReturn(now);
        List<School> schools = IntStream.rangeClosed(1, 10)
            .mapToObj(i -> SchoolFixture.builder().build())
            .toList();

        // when
        List<Festival> actual = mockFestivalsGenerator.generate(schools, 1);

        // then
        assertThat(actual).hasSize(schools.size());
    }
}
