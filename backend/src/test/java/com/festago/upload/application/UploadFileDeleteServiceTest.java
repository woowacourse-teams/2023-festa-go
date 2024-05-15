package com.festago.upload.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.spy;

import com.festago.support.TimeInstantProvider;
import com.festago.support.fixture.UploadFileFixture;
import com.festago.upload.domain.UploadFile;
import com.festago.upload.infrastructure.FakeStorageClient;
import com.festago.upload.repository.MemoryUploadFileRepository;
import com.festago.upload.repository.UploadFileRepository;
import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class UploadFileDeleteServiceTest {

    UploadFileDeleteService uploadFileDeleteService;

    UploadFileRepository uploadFileRepository;

    Clock clock;

    @BeforeEach
    void setUp() {
        uploadFileRepository = new MemoryUploadFileRepository();
        clock = spy(Clock.systemDefaultZone());
        uploadFileDeleteService = new UploadFileDeleteService(
            new FakeStorageClient(),
            uploadFileRepository,
            clock
        );
    }

    @Nested
    class deleteAbandonedStatusWithPeriod {

        LocalDateTime _6월_30일_18시_0분_0초 = LocalDateTime.parse("2077-06-30T18:00:00");
        LocalDateTime _6월_30일_18시_0분_1초 = LocalDateTime.parse("2077-06-30T18:00:01");
        LocalDateTime _6월_30일_18시_0분_2초 = LocalDateTime.parse("2077-06-30T18:00:02");

        @Test
        void 삭제되는_파일은_시작일에_포함되고_종료일에도_포함된다() {
            // given
            UploadFile _6월_30일_18시_0분_0초_생성된_파일 = uploadFileRepository.save(
                UploadFileFixture.builder().createdAt(_6월_30일_18시_0분_0초).buildAbandoned());
            UploadFile _6월_30일_18시_0분_1초_생성된_파일 = uploadFileRepository.save(
                UploadFileFixture.builder().createdAt(_6월_30일_18시_0분_1초).buildAbandoned());
            UploadFile _6월_30일_18시_0분_2초_생성된_파일 = uploadFileRepository.save(
                UploadFileFixture.builder().createdAt(_6월_30일_18시_0분_2초).buildAbandoned());

            // when
            uploadFileDeleteService.deleteAbandonedStatusWithPeriod(_6월_30일_18시_0분_0초, _6월_30일_18시_0분_1초);

            // then
            var expect = uploadFileRepository.findByIdIn(List.of(
                _6월_30일_18시_0분_0초_생성된_파일.getId(),
                _6월_30일_18시_0분_1초_생성된_파일.getId(),
                _6월_30일_18시_0분_2초_생성된_파일.getId()
            ));
            assertThat(expect)
                .map(UploadFile::getId)
                .containsExactly(_6월_30일_18시_0분_2초_생성된_파일.getId());
        }

        @Test
        void ABANDONED_상태의_파일만_삭제된다() {
            // given
            UploadFile UPLOADED_상태_파일 = uploadFileRepository.save(
                UploadFileFixture.builder().createdAt(_6월_30일_18시_0분_0초).build());
            UploadFile ABANDONED_상태_파일 = uploadFileRepository.save(
                UploadFileFixture.builder().createdAt(_6월_30일_18시_0분_0초).buildAbandoned());

            // when
            uploadFileDeleteService.deleteAbandonedStatusWithPeriod(_6월_30일_18시_0분_0초, _6월_30일_18시_0분_0초);

            // then
            assertThat(uploadFileRepository.findById(UPLOADED_상태_파일.getId())).isPresent();
            assertThat(uploadFileRepository.findById(ABANDONED_상태_파일.getId())).isEmpty();
        }
    }

    @Nested
    class deleteOldUploadedStatus {

        LocalDateTime _6월_29일_17시_59분_59초 = LocalDateTime.parse("2077-06-29T17:59:59");
        LocalDateTime _6월_29일_18시_0분_0초 = LocalDateTime.parse("2077-06-29T18:00:00");
        LocalDateTime _6월_29일_18시_0분_1초 = LocalDateTime.parse("2077-06-29T18:00:01");
        LocalDateTime _6월_30일_18시_0분_1초 = LocalDateTime.parse("2077-06-30T18:00:01");

        @Test
        void 생성된지_정확히_하루가_지난_파일만_삭제된다() {
            // given
            UploadFile _6월_29일_17시_59분_59초_생성된_파일 = uploadFileRepository.save(
                UploadFileFixture.builder().createdAt(_6월_29일_17시_59분_59초).build());
            UploadFile _6월_29일_18시_0분_0초_생성된_파일 = uploadFileRepository.save(
                UploadFileFixture.builder().createdAt(_6월_29일_18시_0분_0초).build());
            UploadFile _6월_29일_18시_0분_1초_생성된_파일 = uploadFileRepository.save(
                UploadFileFixture.builder().createdAt(_6월_29일_18시_0분_1초).build());

            LocalDateTime now = _6월_30일_18시_0분_1초;
            given(clock.instant())
                .willReturn(TimeInstantProvider.from(now));

            // when
            uploadFileDeleteService.deleteOldUploadedStatus();

            // then
            var expect = uploadFileRepository.findByIdIn(List.of(
                _6월_29일_17시_59분_59초_생성된_파일.getId(),
                _6월_29일_18시_0분_0초_생성된_파일.getId(),
                _6월_29일_18시_0분_1초_생성된_파일.getId()
            ));
            assertThat(expect)
                .map(UploadFile::getId)
                .containsExactly(_6월_29일_18시_0분_1초_생성된_파일.getId());
        }

        @Test
        void UPLOADED_상태의_파일만_삭제된다() {
            // given
            UploadFile UPLOADED_상태_파일 = uploadFileRepository.save(
                UploadFileFixture.builder().createdAt(_6월_29일_18시_0분_0초).build());
            UploadFile ABANDONED_상태_파일 = uploadFileRepository.save(
                UploadFileFixture.builder().createdAt(_6월_29일_18시_0분_0초).buildAbandoned());

            LocalDateTime now = _6월_30일_18시_0분_1초;
            given(clock.instant())
                .willReturn(TimeInstantProvider.from(now));

            // when
            uploadFileDeleteService.deleteOldUploadedStatus();

            // then
            assertThat(uploadFileRepository.findById(UPLOADED_상태_파일.getId())).isEmpty();
            assertThat(uploadFileRepository.findById(ABANDONED_상태_파일.getId())).isPresent();
        }
    }
}
