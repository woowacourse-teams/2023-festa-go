package com.festago.fcm.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

import com.festago.fcm.domain.MemberFCM;
import com.festago.fcm.dto.MemberFCMResponse;
import com.festago.fcm.repository.MemberFCMRepository;
import com.festago.support.MemberFixture;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MemberFCMServiceTest {

    @Mock
    MemberFCMRepository memberFCMRepository;

    @InjectMocks
    MemberFCMService memberFCMService;

    @Test
    void 유저의_FCM_정보를_가져온다() {
        // given
        List<MemberFCM> memberFCMS = List.of(
            new MemberFCM(1L, MemberFixture.member().build(), "token"),
            new MemberFCM(2L, MemberFixture.member().build(), "token2")
        );
        given(memberFCMRepository.findByMemberId(anyLong()))
            .willReturn(memberFCMS);

        List<MemberFCMResponse> exepct = memberFCMS.stream()
            .map(MemberFCMResponse::from)
            .collect(Collectors.toList());

        // when
        List<MemberFCMResponse> actual = memberFCMService.findMemberFCM(1L).memberFCMs();

        // then
        assertThat(actual).isEqualTo(exepct);
    }
}