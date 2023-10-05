package com.festago.entryalert.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.festago.entryalert.domain.AlertStatus;
import com.festago.entryalert.domain.EntryAlert;
import com.festago.support.EntryAlertFixture;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
@DataJpaTest
class EntryAlertRepositoryTest {

    @Autowired
    EntryAlertRepository entryAlertRepository;

    @Test
    void 상태로_전부_조회() {
        // given
        EntryAlert entryAlert1 = entryAlertRepository.save(
            EntryAlertFixture.entryAlert().status(AlertStatus.PENDING).build());
        EntryAlert entryAlert2 = entryAlertRepository.save(
            EntryAlertFixture.entryAlert().status(AlertStatus.PENDING).build());
        EntryAlert entryAlert3 = entryAlertRepository.save(
            EntryAlertFixture.entryAlert().status(AlertStatus.REQUESTED).build());

        // when
        List<EntryAlert> actual = entryAlertRepository.findAllByStatus(AlertStatus.PENDING);

        // then
        assertThat(actual).containsExactlyInAnyOrder(entryAlert1, entryAlert2);
    }

    @Nested
    class id와_상태로_조회 {

        @Test
        void id와_상태로_조회() {
            // given
            EntryAlert entryAlert = entryAlertRepository.save(
                EntryAlertFixture.entryAlert().status(AlertStatus.PENDING).build());

            // when
            Optional<EntryAlert> actual = entryAlertRepository.findByIdAndStatusForUpdate(
                entryAlert.getId(), AlertStatus.PENDING);

            // then
            assertThat(actual.get()).isEqualTo(entryAlert);
        }

        @Test
        void 상태가_다르면_조회되지_않음() {
            // given
            EntryAlert entryAlert = entryAlertRepository.save(
                EntryAlertFixture.entryAlert().status(AlertStatus.REQUESTED).build());

            // when
            Optional<EntryAlert> actual = entryAlertRepository.findByIdAndStatusForUpdate(
                entryAlert.getId(), AlertStatus.PENDING);

            // then
            assertThat(actual).isEmpty();
        }
    }
}
