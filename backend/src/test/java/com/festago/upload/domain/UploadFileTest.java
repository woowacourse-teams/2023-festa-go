package com.festago.upload.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

import com.festago.common.exception.UnexpectedException;
import com.festago.common.exception.ValidException;
import com.festago.support.fixture.UploadFileFixture;
import java.net.URI;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class UploadFileTest {

    @Nested
    class 생성 {

        private final int size = 1000;
        private final URI location = URI.create("https://festago.com/image.png");
        private final FileExtension extension = FileExtension.PNG;
        private final LocalDateTime createdAt = LocalDateTime.now();

        @Test
        void size가_음수이면_예외() {
            // when & then
            assertThatThrownBy(() -> new UploadFile(-1, location, extension, createdAt))
                .isInstanceOf(ValidException.class);
        }

        @Test
        void location이_null이면_예외() {
            // when & then
            assertThatThrownBy(() -> new UploadFile(size, null, extension, createdAt))
                .isInstanceOf(ValidException.class);
        }

        @Test
        void extension이_null이면_예외() {
            // when & then
            assertThatThrownBy(() -> new UploadFile(size, location, null, createdAt))
                .isInstanceOf(ValidException.class);
        }

        @Test
        void createdAt이_null이면_예외() {
            // when & then
            assertThatThrownBy(() -> new UploadFile(size, location, extension, null))
                .isInstanceOf(ValidException.class);
        }

        @Test
        void 생성된_UploadFile의_상태는_UPLOADED이다() {
            // given
            UploadFile uploadFile = new UploadFile(size, location, extension, createdAt);

            // when & then
            assertThat(uploadFile.getStatus()).isEqualTo(UploadStatus.UPLOADED);
        }
    }

    @Nested
    class 상태_변경 {

        @Test
        void UPLOADED_상태의_UploadFile은_ASSIGNED_상태로_변경할_수_있다() {
            // given
            UploadFile uploadFile = UploadFileFixture.builder().build();

            // when
            uploadFile.changeAssigned(1L, FileOwnerType.FESTIVAL);

            // then
            assertThat(uploadFile.getStatus()).isEqualTo(UploadStatus.ASSIGNED);
        }

        @ParameterizedTest(name = "{1}")
        @MethodSource("uploadFilesWithoutUploaded")
        void UPLOADED_상태가_아닌_UploadFile을_ASSIGNED_상태로_변경을_시도하면_예외가_발생한다(UploadFile uploadFile, UploadStatus status) {
            // when & then
            assertThatThrownBy(() -> uploadFile.changeAssigned(1L, FileOwnerType.FESTIVAL))
                .isInstanceOf(UnexpectedException.class);
        }

        public static Stream<Arguments> uploadFilesWithoutUploaded() {
            return Stream.of(
                Arguments.arguments(UploadFileFixture.builder().buildAssigned(), UploadStatus.ASSIGNED),
                Arguments.arguments(UploadFileFixture.builder().buildAttached(), UploadStatus.ATTACHED),
                Arguments.arguments(UploadFileFixture.builder().buildAbandoned(), UploadStatus.ABANDONED)
            );
        }

        @Test
        void UPLOADED_상태의_UploadFile은_ATTACHED_상태로_변경할_수_있다() {
            // given
            UploadFile uploadFile = UploadFileFixture.builder().build();

            // when
            uploadFile.changeAttached(1L, FileOwnerType.FESTIVAL);

            // then
            assertThat(uploadFile.getStatus()).isEqualTo(UploadStatus.ATTACHED);
        }

        @ParameterizedTest(name = "{1}")
        @MethodSource("uploadFilesWithoutUploaded")
        void UPLOADED_상태가_아닌_UploadFile을_ATTACHED_상태로_변경을_시도하면_예외가_발생한다(UploadFile uploadFile, UploadStatus status) {
            // when & then
            assertThatThrownBy(() -> uploadFile.changeAttached(1L, FileOwnerType.FESTIVAL))
                .isInstanceOf(UnexpectedException.class);
        }

        @ParameterizedTest(name = "{1}")
        @MethodSource("uploadFiles")
        void 모든_상태의_UploadFile은_ABANDONED_상태로_변경할_수_있다(UploadFile uploadFile, UploadStatus status) {
            // when
            uploadFile.changeAbandoned();

            // then
            assertThat(uploadFile.getStatus()).isEqualTo(UploadStatus.ABANDONED);
        }

        public static Stream<Arguments> uploadFiles() {
            return Stream.of(
                Arguments.arguments(UploadFileFixture.builder().build(), UploadStatus.UPLOADED),
                Arguments.arguments(UploadFileFixture.builder().buildAssigned(), UploadStatus.ASSIGNED),
                Arguments.arguments(UploadFileFixture.builder().buildAttached(), UploadStatus.ATTACHED),
                Arguments.arguments(UploadFileFixture.builder().buildAbandoned(), UploadStatus.ABANDONED)
            );
        }
    }

    @Nested
    class renewalStatus {

        @Test
        void UPLOADED_상태의_파일은_상태가_변경되지_않는다() {
            // given
            UploadFile uploadFile = UploadFileFixture.builder().build();

            // when
            uploadFile.renewalStatus(Set.of(uploadFile.getId()));

            // then
            assertThat(uploadFile.getStatus()).isEqualTo(UploadStatus.UPLOADED);
        }

        @Test
        void ABANDONED_상태의_파일은_상태가_변경되지_않는다() {
            // given
            UploadFile uploadFile = UploadFileFixture.builder().buildAbandoned();

            // when
            uploadFile.renewalStatus(Set.of(uploadFile.getId()));

            // then
            assertThat(uploadFile.getStatus()).isEqualTo(UploadStatus.ABANDONED);
        }

        @Test
        void ATTACHED_상태의_파일이고_식별자_목록에_자신이_포함되면_상태가_변하지_않는다() {
            // given
            UploadFile uploadFile = UploadFileFixture.builder().buildAttached();

            // when
            uploadFile.renewalStatus(Set.of(uploadFile.getId()));

            // then
            assertThat(uploadFile.getStatus()).isEqualTo(UploadStatus.ATTACHED);
        }

        @Test
        void ATTACHED_상태의_파일이고_식별자_목록에_자신이_포함되지_않으면_ABANDONED_상태로_변경된다() {
            // given
            UploadFile uploadFile = UploadFileFixture.builder().buildAttached();

            // when
            uploadFile.renewalStatus(Collections.emptySet());

            // then
            assertThat(uploadFile.getStatus()).isEqualTo(UploadStatus.ABANDONED);
        }

        @Test
        void ASSIGNED_상태의_파일이고_식별자_목록에_자신이_포함되면_ATTACHED_상태로_변경된다() {
            // given
            UploadFile uploadFile = UploadFileFixture.builder().buildAssigned();

            // when
            uploadFile.renewalStatus(Set.of(uploadFile.getId()));

            // then
            assertThat(uploadFile.getStatus()).isEqualTo(UploadStatus.ATTACHED);
        }

        @Test
        void ASSIGNED_상태의_파일이고_식별자_목록에_자신이_포함되지_않으면_ABANDONED_상태로_변경된다() {
            // given
            UploadFile uploadFile = UploadFileFixture.builder().buildAssigned();

            // when
            uploadFile.renewalStatus(Collections.emptySet());

            // then
            assertThat(uploadFile.getStatus()).isEqualTo(UploadStatus.ABANDONED);
        }
    }

    @Nested
    class getUploadUri {

        @Test
        void location에_날짜와_식별자와_확장자가_붙은_형식으로_반환된다() {
            // given
            UploadFile uploadFile = UploadFileFixture.builder()
                .createdAt(LocalDateTime.parse("2077-06-30T18:00:00"))
                .location(URI.create("https://festago.com"))
                .extension(FileExtension.PNG)
                .build();

            // when
            URI uploadUri = uploadFile.getUploadUri();

            // then
            UUID id = uploadFile.getId();
            assertThat(uploadUri).hasToString("https://festago.com/2077-06-30/" + id + ".png");
        }
    }

    @Nested
    class getName {

        @Test
        void 날짜와_식별자와_확장자가_붙은_형식으로_반환된다() {
            // given
            UploadFile uploadFile = UploadFileFixture.builder()
                .createdAt(LocalDateTime.parse("2077-06-30T18:00:00"))
                .location(URI.create("https://festago.com"))
                .extension(FileExtension.PNG)
                .build();

            // when
            String filename = uploadFile.getName();

            // then
            UUID id = uploadFile.getId();
            assertThat(filename).isEqualTo("2077-06-30/" + id + ".png");
        }
    }
}
