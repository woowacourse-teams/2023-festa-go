package com.festago.festago.presentation.ticketentry

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.festago.festago.domain.model.TicketCode
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
        println("${vm.ticketCode.value}")

        // THEN 티켓 코드를 받는다.
        assert(vm.ticketCode.value == fakeTicketCode.toPresentation())
    }
}
