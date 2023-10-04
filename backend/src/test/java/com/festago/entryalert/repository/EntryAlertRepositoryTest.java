package com.festago.entryalert.repository;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
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
    }
}
