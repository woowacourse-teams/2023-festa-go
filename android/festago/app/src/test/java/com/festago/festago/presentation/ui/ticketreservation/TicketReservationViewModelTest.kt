package com.festago.festago.presentation.ui.ticketreservation

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.festago.festago.domain.model.Reservation
import com.festago.festago.domain.repository.ReservationRepository
import com.festago.festago.presentation.model.ReservationUiModel
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

class TicketReservationViewModelTest {
    private lateinit var vm: TicketReservationViewModel
    private lateinit var reservationRepository: ReservationRepository

    private val fakeReservation = Reservation(0)

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setUp() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
        reservationRepository = mockk()
        vm = TicketReservationViewModel(reservationRepository)
    }

    @Test
    fun `예약 정보를 불러오면 성공 이벤트가 발생하고 리스트를 반환한다`() {
        // given
        coEvery {
            reservationRepository.loadReservation()
        } answers {
            Result.success(List(10) { fakeReservation })
        }

        // when
        vm.loadReservation()

        // then
        assertThat(vm.uiState.getValue()).isInstanceOf(TicketReservationUiState.Success::class.java)

        // and
        val uiModel = vm.uiState.getValue() as TicketReservationUiState.Success
        assertThat(uiModel.reservations).isEqualTo(List(10) { ReservationUiModel(0) })
    }

    @Test
    fun `예약 정보를 불러오는 것을 실패하면 에러 이벤트가 발생한다`() {
        // given
        coEvery { reservationRepository.loadReservation() } returns Result.failure(Exception())

        // when
        vm.loadReservation()

        // then
        assertThat(vm.uiState.getValue()).isEqualTo(TicketReservationUiState.Error)
    }

    @Test
    fun `예약 정보를 불러오는 중이면 로딩 이벤트가 발생한다`() {
        // given
        coEvery {
            reservationRepository.loadReservation()
        } coAnswers {
            delay(1000)
            Result.success(List(10) { fakeReservation })
        }

        // when
        vm.loadReservation()

        // then
        assertThat(vm.uiState.getValue()).isEqualTo(TicketReservationUiState.Loading)
    }
}
