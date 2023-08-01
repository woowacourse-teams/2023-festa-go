package com.festago.festago.presentation.ui.ticketreserve

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.festago.festago.domain.model.Reservation
import com.festago.festago.domain.model.ReservationStage
import com.festago.festago.domain.model.ReservationTicket
import com.festago.festago.domain.repository.ReservationRepository
import com.festago.festago.presentation.mapper.toPresentation
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
import java.time.LocalDateTime

class TicketReserveViewModelTest {
    private lateinit var vm: TicketReserveViewModel
    private lateinit var reservationRepository: ReservationRepository

    private val fakeReservationTickets = listOf(
        ReservationTicket("재학생용", 219, 500),
        ReservationTicket("외부인용", 212, 300),
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
        startDate = LocalDateTime.now(),
        endDate = LocalDateTime.now(),
        thumbnail = "https://search2.kakaocdn.net/argon/656x0_80_wr/8vLywd3V06c",
    )

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setUp() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
        reservationRepository = mockk()
        vm = TicketReserveViewModel(reservationRepository)
    }

    @Test
    fun `예약 정보를 불러오면 성공 이벤트가 발생하고 리스트를 반환한다`() {
        // given
        coEvery {
            reservationRepository.loadReservation()
        } answers {
            Result.success(fakeReservation)
        }

        // when
        vm.loadReservation()

        // then
        assertThat(vm.uiState.value).isInstanceOf(TicketReserveUiState.Success::class.java)

        // and
        val uiModel = vm.uiState.value as TicketReserveUiState.Success
        assertThat(uiModel.reservation).isEqualTo(fakeReservation.toPresentation())
    }

    @Test
    fun `예약 정보를 불러오는 것을 실패하면 에러 이벤트가 발생한다`() {
        // given
        coEvery { reservationRepository.loadReservation() } returns Result.failure(Exception())

        // when
        vm.loadReservation()

        // then
        assertThat(vm.uiState.value).isEqualTo(TicketReserveUiState.Error)
    }

    @Test
    fun `예약 정보를 불러오는 중이면 로딩 이벤트가 발생한다`() {
        // given
        coEvery {
            reservationRepository.loadReservation()
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
            reservationRepository.loadReservation()
        } answers {
            Result.success(fakeReservation)
        }

        // when
        vm.showTicketTypes(1)

        // then
        assertThat(vm.event.getValue()).isInstanceOf(TicketReserveEvent.ShowTicketTypes::class.java)

        // and
        val event = vm.event.getValue() as TicketReserveEvent.ShowTicketTypes
        assertThat(event.reservationStage).isEqualTo(fakeReservationStage.toPresentation())
    }

    @Test
    fun `특정 공연의 티켓 타입을 보여주는 것을 실패하면 에러 이벤트가 발생한다`() {
        // given
        coEvery { reservationRepository.loadReservation() } returns Result.failure(Exception())

        // when
        vm.showTicketTypes(1)

        // then
        assertThat(vm.uiState.value).isEqualTo(TicketReserveUiState.Error)
    }

    @Test
    fun `티켓 유형을 선택하고 예약하면 예약 성공 이벤트가 발생한다`() {
        // given
        coEvery {
            reservationRepository.reserveTicket(0, 0)
        } answers {
            Result.success(1)
        }

        // when
        vm.reserveTicket()

        // then
        assertThat(vm.event.getValue()).isEqualTo(TicketReserveEvent.ReserveTicketSuccess)
    }

    @Test
    fun `티켓 유형을 선택하고 예약하는 것을 실패하면 예약 실패 이벤트가 발생한다`() {
        // given
        coEvery {
            reservationRepository.reserveTicket(0, 0)
        } answers {
            Result.failure(Exception())
        }

        // when
        vm.reserveTicket()

        // then
        assertThat(vm.event.getValue()).isEqualTo(TicketReserveEvent.ReserveTicketFailed)
    }
}
