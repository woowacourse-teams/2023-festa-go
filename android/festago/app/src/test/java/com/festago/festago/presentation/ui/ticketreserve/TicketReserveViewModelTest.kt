package com.festago.festago.presentation.ui.ticketreserve

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.festago.festago.analytics.AnalyticsHelper
import com.festago.festago.model.Reservation
import com.festago.festago.model.ReservationStage
import com.festago.festago.model.ReservationTicket
import com.festago.festago.model.ReservedTicket
import com.festago.festago.repository.AuthRepository
import com.festago.festago.repository.FestivalRepository
import com.festago.festago.repository.ReservationTicketRepository
import com.festago.festago.repository.TicketRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.setMain
import org.assertj.core.api.AssertionsForClassTypes.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.time.LocalDate
import java.time.LocalDateTime

class TicketReserveViewModelTest {
    private lateinit var vm: TicketReserveViewModel
    private lateinit var reservationTicketRepository: ReservationTicketRepository
    private lateinit var festivalRepository: FestivalRepository
    private lateinit var ticketRepository: TicketRepository
    private lateinit var authRepository: AuthRepository
    private lateinit var analyticsHelper: AnalyticsHelper

    private val fakeReservationTickets = listOf(
        ReservationTicket(1, "재학생용", 219, 500),
        ReservationTicket(1, "외부인용", 212, 300),
    )
    private val fakeReservationStage = ReservationStage(
        id = 1,
        lineUp = "르세라핌, 아이브, 뉴진스",
        reservationTickets = fakeReservationTickets,
        startTime = LocalDateTime.now(),
        ticketOpenTime = LocalDateTime.now(),
    )
    private val fakeReservationStages = List(5) { fakeReservationStage }
    private val fakeReservation = Reservation(
        id = 1,
        name = "테코대학교",
        reservationStages = fakeReservationStages,
        startDate = LocalDate.now(),
        endDate = LocalDate.now(),
        thumbnail = "https://search2.kakaocdn.net/argon/656x0_80_wr/8vLywd3V06c",
    )

    private val fakeReservedTicket = ReservedTicket(
        id = 1,
        entryTime = LocalDateTime.now(),
        number = 1,
    )

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setUp() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
        reservationTicketRepository = mockk()
        festivalRepository = mockk()
        ticketRepository = mockk()
        authRepository = mockk()
        analyticsHelper = mockk(relaxed = true)
        vm = TicketReserveViewModel(
            reservationTicketRepository,
            festivalRepository,
            ticketRepository,
            authRepository,
            analyticsHelper,
        )
    }

    @Test
    fun `예약 정보를 불러오면 성공 이벤트가 발생하고 리스트를 반환한다`() {
        // given
        coEvery {
            festivalRepository.loadFestivalDetail(0)
        } answers {
            Result.success(fakeReservation)
        }

        coEvery {
            authRepository.isSigned
        } answers {
            true
        }

        // when
        vm.loadReservation()

        // then
        assertThat(vm.uiState.value).isInstanceOf(TicketReserveUiState.Success::class.java)

        // and
        val festival = (vm.uiState.value as TicketReserveUiState.Success).festival
        val expected = ReservationFestivalUiState(
            id = festival.id,
            name = festival.name,
            thumbnail = festival.thumbnail,
            endDate = festival.endDate,
            startDate = festival.startDate,
        )
        assertThat(festival).isEqualTo(expected)
    }

    @Test
    fun `예약 정보를 불러오는 것을 실패하면 에러 이벤트가 발생한다`() {
        // given
        coEvery { festivalRepository.loadFestivalDetail(0) } returns Result.failure(Exception())

        // when
        vm.loadReservation(0)

        // then
        assertThat(vm.uiState.value).isEqualTo(TicketReserveUiState.Error)
    }

    @Test
    fun `예약 정보를 불러오는 중이면 로딩 이벤트가 발생한다`() {
        // given
        coEvery {
            festivalRepository.loadFestivalDetail(0)
        } coAnswers {
            delay(1000)
            Result.success(fakeReservation)
        }

        // when
        vm.loadReservation()

        // then
        assertThat(vm.uiState.value).isEqualTo(TicketReserveUiState.Loading)
    }

    @Test
    fun `특정 공연의 티켓 타입을 보여주는 이벤트가 발생하면 해당 공연의 티켓 타입을 보여준다`() {
        // given
        coEvery {
            reservationTicketRepository.loadTicketTypes(1)
        } answers {
            Result.success(fakeReservationTickets)
        }

        coEvery {
            authRepository.isSigned
        } answers {
            true
        }

        // when
        vm.showTicketTypes(1, LocalDateTime.MIN)

        // then
        assertThat(vm.event.getValue()).isInstanceOf(TicketReserveEvent.ShowTicketTypes::class.java)

        // and
        val event = vm.event.getValue() as TicketReserveEvent.ShowTicketTypes
        assertThat(event.tickets).isEqualTo(fakeReservationTickets)
    }

    @Test
    fun `특정 공연의 티켓 타입을 보여주는 것을 실패하면 에러 이벤트가 발생한다`() {
        // given
        coEvery { reservationTicketRepository.loadTicketTypes(1) } returns Result.failure(Exception())

        coEvery {
            authRepository.isSigned
        } answers {
            true
        }

        // when
        vm.showTicketTypes(1, LocalDateTime.MIN)

        // then
        assertThat(vm.uiState.value).isEqualTo(TicketReserveUiState.Error)
    }

    @Test
    fun `티켓 유형을 선택하고 예약하면 예약 성공 이벤트가 발생한다`() {
        // given
        coEvery {
            ticketRepository.reserveTicket(any())
        } answers {
            Result.success(fakeReservedTicket)
        }
        // when
        vm.reserveTicket(0)

        // then
        assertThat(vm.event.getValue()).isInstanceOf(TicketReserveEvent.ReserveTicketSuccess::class.java)
    }

    @Test
    fun `티켓 유형을 선택하고 예약하는 것을 실패하면 예약 실패 이벤트가 발생한다`() {
        // given
        coEvery {
            ticketRepository.reserveTicket(0)
        } answers {
            Result.failure(Exception())
        }

        // when
        vm.reserveTicket(0)

        // then
        assertThat(vm.event.getValue()).isEqualTo(TicketReserveEvent.ReserveTicketFailed)
    }
}
