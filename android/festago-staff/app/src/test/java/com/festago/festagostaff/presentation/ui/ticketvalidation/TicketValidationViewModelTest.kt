package com.festago.festagostaff.presentation.ui.ticketvalidation

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.festago.festagostaff.domain.model.TicketState
import com.festago.festagostaff.domain.repository.TicketRepository
import io.mockk.coEvery
import io.mockk.coJustRun
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.setMain
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class TicketValidationViewModelTest {
    private lateinit var vm: TicketValidationViewModel
    private lateinit var ticketRepository: TicketRepository

    private val fakeTicketState = TicketState.AFTER_ENTRY
    private val fakeCode = "XXX"

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
        ticketRepository = mockk()
        vm = TicketValidationViewModel(ticketRepository)
    }

    @Test
    fun `티켓 코드 검증을 하면 새로운 티켓 상태를 불러올 수 있다`() {
        // given
        coEvery { ticketRepository.validateTicket(any()) } returns fakeTicketState

        // when
        vm.validateTicketCode(fakeCode)

        // then
        assertThat(vm.ticketState.value).isEqualTo(fakeTicketState.toString())
    }

    @Test
    fun `티켓 코드를 검증을 하면 검증한 코드는 최근 검증 코드와 같다`() {
        // given
        coJustRun { ticketRepository.validateTicket(any()) }

        // when
        vm.validateTicketCode(fakeCode)

        // then
        assertThat(vm.isLatestCode(fakeCode)).isTrue
    }

    @Test
    fun `티켓 코드 검증 후 최근 검증 코드를 초기화하면 검증한 코드는 최근 검증 코드와 다르다`() {
        // given
        coJustRun { ticketRepository.validateTicket(any()) }

        // when
        vm.validateTicketCode(fakeCode)
        vm.clearLatestCode()

        // then
        assertThat(vm.isLatestCode(fakeCode)).isFalse
    }
}
