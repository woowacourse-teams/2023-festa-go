package com.festago.ticketing.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.festago.common.domain.BaseTimeEntity;
import com.festago.config.JpaAuditingConfig;
import com.festago.festival.domain.Festival;
import com.festago.festival.repository.FestivalRepository;
import com.festago.member.domain.Member;
import com.festago.member.repository.MemberRepository;
import com.festago.school.domain.School;
import com.festago.school.repository.SchoolRepository;
import com.festago.stage.domain.Stage;
import com.festago.stage.repository.StageRepository;
import com.festago.support.FestivalFixture;
import com.festago.support.MemberFixture;
import com.festago.support.MemberTicketFixture;
import com.festago.support.RepositoryTest;
import com.festago.support.SchoolFixture;
import com.festago.support.StageFixture;
import com.festago.ticket.repository.TicketRepository;
import com.festago.ticketing.domain.MemberTicket;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@Import(JpaAuditingConfig.class)
@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
@RepositoryTest
class MemberTicketRepositoryTest {

    @Autowired
    MemberTicketRepository memberTicketRepository;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    TicketRepository ticketRepository;

    @Autowired
    StageRepository stageRepository;

    @Autowired
    FestivalRepository festivalRepository;

    @Autowired
    SchoolRepository schoolRepository;

    @Nested
    class 회원의_ID로_에매한_티켓을_모두_조회 {

        @Test
        void 성공() {
            // given
            Member member1 = memberRepository.save(MemberFixture.member().socialId("abc").build());
            Member member2 = memberRepository.save(MemberFixture.member().socialId("def").build());

            School school = schoolRepository.save(SchoolFixture.school().build());
            Festival festival = festivalRepository.save(FestivalFixture.festival().school(school).build());
            Stage stage1 = stageRepository.save(StageFixture.stage().festival(festival).build());
            Stage stage2 = stageRepository.save(StageFixture.stage().festival(festival).build());

            memberTicketRepository.save(MemberTicketFixture.memberTicket().stage(stage1).owner(member1).build());
            memberTicketRepository.save(MemberTicketFixture.memberTicket().stage(stage2).owner(member1).build());
            memberTicketRepository.save(MemberTicketFixture.memberTicket().stage(stage1).owner(member2).build());

            // when
            List<MemberTicket> memberTickets = memberTicketRepository.findAllByOwnerId(member1.getId(),
                PageRequest.of(0, 10));

            // then
            assertThat(memberTickets).hasSize(2);
        }

        @Test
        void 지정한_갯수만큼_조회() {
            // given
            int expected = 10;
            Member member = memberRepository.save(MemberFixture.member().build());

            School school = schoolRepository.save(SchoolFixture.school().build());
            Festival festival = festivalRepository.save(FestivalFixture.festival().school(school).build());
            Stage stage = stageRepository.save(StageFixture.stage().festival(festival).build());

            for (int i = 0; i < 20; i++) {
                memberTicketRepository.save(MemberTicketFixture.memberTicket().stage(stage).owner(member).build());
            }

            // when
            List<MemberTicket> memberTickets = memberTicketRepository.findAllByOwnerId(member.getId(),
                PageRequest.of(0, expected));

            // then
            assertThat(memberTickets).hasSize(expected);
        }

        @Test
        void 지정한_정렬으로_조회() {
            // given
            Member member = memberRepository.save(MemberFixture.member().build());

            School school = schoolRepository.save(SchoolFixture.school().build());
            Festival festival = festivalRepository.save(FestivalFixture.festival().school(school).build());
            Stage stage = stageRepository.save(StageFixture.stage().festival(festival).build());

            List<MemberTicket> memberTickets = new ArrayList<>();
            for (int i = 0; i < 20; i++) {
                memberTickets.add(MemberTicketFixture.memberTicket().stage(stage).owner(member).build());
            }
            memberTicketRepository.saveAll(memberTickets);

            Pageable pageable = PageRequest.of(0, 100, Sort.by("entryTime").descending());

            // when
            List<MemberTicket> actual = memberTicketRepository.findAllByOwnerId(member.getId(), pageable);

            // then
            List<MemberTicket> expected = memberTickets.stream()
                .sorted(Comparator.comparing(BaseTimeEntity::getCreatedAt).reversed())
                .toList();

            assertThat(actual).isEqualTo(expected);
        }
    }
}
