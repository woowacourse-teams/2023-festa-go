package com.festago.festago.presentation.ticketentry

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.festago.festago.domain.model.Stage
import com.festago.festago.domain.model.Ticket
import com.festago.festago.domain.model.TicketCode
import com.festago.festago.domain.model.TicketState
import com.festago.festago.domain.repository.TicketRepository
import com.festago.festago.presentation.mapper.toPresentation
import com.festago.festago.presentation.ui.ticketentry.TicketEntryViewModel
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.time.LocalDateTime

class TicketEntryViewModelTest {
    private lateinit var vm: TicketEntryViewModel
    private lateinit var ticketRepository: TicketRepository

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setUp() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
        ticketRepository = mockk()
        vm = TicketEntryViewModel(ticketRepository)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @After
    fun finish() {
        Dispatchers.resetMain()
    }

    @Test
    fun `티켓 코드를 받아온다`() {
        // GIVEN 티켓 ID 를 가지고 있다.
        val ticketId = 1L
        val fakeTicketCode = TicketCode("test.com", 30)
        coEvery { ticketRepository.loadTicketCode(any()) } returns fakeTicketCode

        // WHEN Ticket
        vm.loadTicketCode(ticketId)

        // THEN 티켓 코드를 받는다.
        assert(vm.ticketCode.value == fakeTicketCode.toPresentation())
    }

    @Test
    fun `티켓을 받아온다`() {
        // GIVEN 티켓 ID 를 가지고 있다.
        val ticketId = 0L
        val fakeStage = Stage(ticketId, "fakeStage", LocalDateTime.MIN)
        val fakeTicket =
            Ticket(ticketId, 0, LocalDateTime.MIN, TicketState.AWAY, fakeStage)

        coEvery { ticketRepository.loadTicket(any()) } returns fakeTicket

        // WHEN 티켓을 요청한다.
        vm.loadTicket(ticketId)

        // THEN 티켓을 받는다.
        assert(vm.ticket.value == fakeTicket.toPresentation())
    }
}
