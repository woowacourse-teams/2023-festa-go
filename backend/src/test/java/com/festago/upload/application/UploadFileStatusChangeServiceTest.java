package com.festago.upload.application;

import static com.festago.upload.domain.FileOwnerType.FESTIVAL;
import static org.assertj.core.api.Assertions.assertThat;

import com.festago.festival.domain.Festival;
import com.festago.festival.repository.FestivalRepository;
import com.festago.festival.repository.MemoryFestivalRepository;
import com.festago.support.fixture.FestivalFixture;
import com.festago.support.fixture.UploadFileFixture;
import com.festago.upload.domain.FileOwnerType;
import com.festago.upload.domain.UploadFile;
import com.festago.upload.domain.UploadStatus;
import com.festago.upload.repository.MemoryUploadFileRepository;
import com.festago.upload.repository.UploadFileRepository;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class UploadFileStatusChangeServiceTest {

    FestivalRepository festivalRepository;

    UploadFileRepository uploadFileRepository;

    UploadFileStatusChangeService uploadFileStatusChangeService;

    @BeforeEach
    void setUp() {
        festivalRepository = new MemoryFestivalRepository();
        uploadFileRepository = new MemoryUploadFileRepository();
        uploadFileStatusChangeService = new UploadFileStatusChangeService(
            uploadFileRepository
        );
    }

    @Nested
    class changeAttached {

        @Test
        void 주인이_가진_이미지가_업로드된_파일이면_ATTACHED_상태로_변경된다() {
            // given
            UploadFile uploadFile = UploadFileFixture.builder().build();
            uploadFileRepository.save(uploadFile);
            Festival festival = FestivalFixture.builder()
                .posterImageUrl(uploadFile.getUploadUri().toString())
                .build();
            festivalRepository.save(festival);

            // when
            Long festivalId = festival.getId();
            List<String> festivalImages = List.of(festival.getPosterImageUrl());
            uploadFileStatusChangeService.changeAttached(festivalId, FESTIVAL, festivalImages);

            // then
            assertThat(uploadFile.getStatus()).isEqualTo(UploadStatus.ATTACHED);
            assertThat(uploadFile.getOwnerId()).isEqualTo(festival.getId());
        }
    }

    @Nested
    class changeRenewal {

        @Test
        void 주인이_파일을_가지고_있고_ASSIGNED_상태이면_ATTACHED_상태로_변경된다() {
            // given
            UploadFile uploadFile = UploadFileFixture.builder().build();
            uploadFileRepository.save(uploadFile);
            Festival festival = FestivalFixture.builder()
                .posterImageUrl(uploadFile.getUploadUri().toString())
                .build();
            festivalRepository.save(festival);
            uploadFile.changeAssigned(festival.getId(), FESTIVAL);

            // when
            Long festivalId = festival.getId();
            List<String> festivalImages = List.of(festival.getPosterImageUrl());
            uploadFileStatusChangeService.changeRenewal(festivalId, FESTIVAL, festivalImages);

            // then
            assertThat(uploadFile.getStatus()).isEqualTo(UploadStatus.ATTACHED);
        }

        @Test
        void 주인이_파일을_가지고_있지_있고_ASSIGNED_상태이면_ABANDONED_상태로_변경된다() {
            // given
            Festival festival = FestivalFixture.builder()
                .build();
            festivalRepository.save(festival);

            UploadFile uploadFile = UploadFileFixture.builder().build();
            uploadFile.changeAssigned(festival.getId(), FESTIVAL);
            uploadFileRepository.save(uploadFile);


            // when
            Long festivalId = festival.getId();
            List<String> festivalImages = List.of(festival.getPosterImageUrl());
            uploadFileStatusChangeService.changeRenewal(festivalId, FESTIVAL, festivalImages);

            // then
            assertThat(uploadFile.getStatus()).isEqualTo(UploadStatus.ABANDONED);
        }
    }

    @Nested
    class changeAllAbandoned {

        @Test
        void 주인의_모든_파일을_ABANDONED_상태로_변경한다() {
            // given
            UploadFile prevUploadFile = UploadFileFixture.builder().build();
            UploadFile uploadFile = UploadFileFixture.builder().build();
            Festival festival = FestivalFixture.builder()
                .posterImageUrl(uploadFile.getUploadUri().toString())
                .build();
            festivalRepository.save(festival);

            prevUploadFile.changeAssigned(festival.getId(), FESTIVAL);
            uploadFile.changeAttached(festival.getId(), FESTIVAL);
            uploadFileRepository.save(prevUploadFile);
            uploadFileRepository.save(uploadFile);

            // when
            uploadFileStatusChangeService.changeAllAbandoned(festival.getId(), FESTIVAL);

            // then
            assertThat(uploadFileRepository.findAllByOwnerIdAndOwnerType(festival.getId(), FESTIVAL))
                .map(UploadFile::getStatus)
                .containsOnly(UploadStatus.ABANDONED);
        }
    }
}
