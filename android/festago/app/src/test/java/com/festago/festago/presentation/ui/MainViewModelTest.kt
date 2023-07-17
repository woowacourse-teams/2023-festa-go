package com.festago.festago.presentation.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.festago.festago.domain.model.Stage
import com.festago.festago.domain.model.Ticket
import com.festago.festago.domain.model.TicketState
import com.festago.festago.domain.repository.TicketRepository
import com.festago.festago.presentation.mapper.toPresentation
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.setMain
import org.assertj.core.api.AssertionsForClassTypes.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.lang.Exception
import java.time.LocalDateTime

class MainViewModelTest {
    private lateinit var vm: MainViewModel
    private lateinit var ticketRepository: TicketRepository

    private val fakeStage = Stage(0L, "fakeStage", LocalDateTime.MIN)

    private val fakeTicket =
        Ticket(0L, 0, LocalDateTime.MIN, TicketState.AWAY, fakeStage)

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setUp() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
        ticketRepository = mockk()
        vm = MainViewModel(ticketRepository)
    }

    @Test
    fun `티켓 id를 전달하면 티켓의 정보를 불러올 수 있다`() {
        // given
        coEvery { ticketRepository.loadTicket(any()) } returns fakeTicket

        // when
        vm.loadTicket()

        // then
        assertThat(vm.ticket.getValue()).isEqualTo(fakeTicket.toPresentation())
    }

    @Test
    fun `티켓 정보를 불러오는 것을 실패하면 에러 이벤트가 발생한다`() {
        // given
        coEvery { ticketRepository.loadTicket(any()) } throws Exception()

        // when
        vm.loadTicket()

        // then
        assertThat(vm.event.getValue()).isInstanceOf(MainEvent.FailToLoadTicket::class.java)
    }

    @Test
    fun `티켓을 제시하면 티켓 확인 이벤트가 발생한다`() {
        // given
        coEvery { ticketRepository.loadTicket(any()) } returns fakeTicket
        vm.loadTicket()

        // when
        vm.openTicketEntry()

        // then
        assertThat(vm.event.getValue()).isInstanceOf(MainEvent.OpenTicketEntry::class.java)
    }

    @Test
    fun `티켓 정보를 불러오지 않고 티켓을 제시를 시도하면 에러 이벤트가 발생한다`() {
        // given

        // when
        vm.openTicketEntry()

        // then
        assertThat(vm.event.getValue()).isInstanceOf(MainEvent.FailToOpenTicketEntry::class.java)
    }
}
