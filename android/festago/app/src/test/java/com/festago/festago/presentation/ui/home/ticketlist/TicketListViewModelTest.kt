package com.festago.festago.presentation.ui.home.ticketlist

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.festago.festago.analytics.AnalyticsHelper
import com.festago.festago.model.Ticket
import com.festago.festago.presentation.fixture.TicketFixture
import com.festago.festago.repository.TicketRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.SoftAssertions
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class TicketListViewModelTest {
    private lateinit var vm: TicketListViewModel
    private lateinit var ticketRepository: TicketRepository
    private lateinit var analyticsHelper: AnalyticsHelper

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setUp() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
        ticketRepository = mockk()
        analyticsHelper = mockk(relaxed = true)
        vm = TicketListViewModel(ticketRepository, analyticsHelper)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @After
    fun finish() {
        Dispatchers.resetMain()
    }

    @Test
    fun `티켓을 받아왔을 때 티켓이 있으면 성공이고 티켓도 존재하는 상태이다`() {
        // given
        val tickets = TicketFixture.getMemberTickets((1L..10L).toList())

        coEvery { ticketRepository.loadCurrentTickets() } returns Result.success(tickets)

        // when
        vm.loadCurrentTickets()

        // then
        val softly = SoftAssertions().apply {
            assertThat(vm.uiState.value).isInstanceOf(TicketListUiState.Success::class.java)

            // and
            assertThat(vm.uiState.value?.shouldShowSuccessWithTickets).isEqualTo(true)
            assertThat(vm.uiState.value?.shouldShowSuccessAndEmpty).isEqualTo(false)
            assertThat(vm.uiState.value?.shouldShowLoading).isEqualTo(false)
            assertThat(vm.uiState.value?.shouldShowError).isEqualTo(false)

            // and
            val actual = (vm.uiState.value as TicketListUiState.Success).tickets
            val expected = tickets.map { TicketListItemUiState.of(it, vm::showTicketEntry) }
            assertThat(actual).isEqualTo(expected)
        }
        softly.assertAll()
    }

    @Test
    fun `티켓을 받아왔을 때 티켓이 없으면 성공이지만 티켓은 없는 상태이다`() {
        // given
        val fakeEmptyTickets = emptyList<Ticket>()
        coEvery { ticketRepository.loadCurrentTickets() } returns Result.success(fakeEmptyTickets)

        // when
        vm.loadCurrentTickets()

        // then
        val softly = SoftAssertions().apply {
            assertThat(vm.uiState.value).isInstanceOf(TicketListUiState.Success::class.java)

            // and
            assertThat(vm.uiState.value?.shouldShowSuccessWithTickets).isEqualTo(false)
            assertThat(vm.uiState.value?.shouldShowSuccessAndEmpty).isEqualTo(true)
            assertThat(vm.uiState.value?.shouldShowLoading).isEqualTo(false)
            assertThat(vm.uiState.value?.shouldShowError).isEqualTo(false)

            // and
            val actual = (vm.uiState.value as TicketListUiState.Success).tickets
            val expected =
                fakeEmptyTickets.map { TicketListItemUiState.of(it, vm::showTicketEntry) }
            assertThat(actual).isEqualTo(expected)
        }
        softly.assertAll()
    }

    @Test
    fun `티켓 목록 받아오기를 실패하면 에러 상태이다`() {
        // given
        coEvery { ticketRepository.loadCurrentTickets() } answers {
            Result.failure(Exception())
        }

        // when
        vm.loadCurrentTickets()

        // then
        val softly = SoftAssertions().apply {
            assertThat(vm.uiState.value).isInstanceOf(TicketListUiState.Error::class.java)

            // and
            assertThat(vm.uiState.value?.shouldShowSuccessWithTickets).isEqualTo(false)
            assertThat(vm.uiState.value?.shouldShowSuccessAndEmpty).isEqualTo(false)
            assertThat(vm.uiState.value?.shouldShowLoading).isEqualTo(false)
            assertThat(vm.uiState.value?.shouldShowError).isEqualTo(true)
        }
        softly.assertAll()
    }

    @Test
    fun `티켓 목록을 받아오는 중이면 로딩 상태이다`() {
        // given
        val tickets = TicketFixture.getMemberTickets((1L..10L).toList())

        coEvery { ticketRepository.loadCurrentTickets() } coAnswers {
            delay(1000)
            Result.success(tickets)
        }

        // when
        vm.loadCurrentTickets()

        // then
        val softly = SoftAssertions().apply {
            assertThat(vm.uiState.value).isInstanceOf(TicketListUiState.Loading::class.java)

            // and
            assertThat(vm.uiState.value?.shouldShowSuccessWithTickets).isEqualTo(false)
            assertThat(vm.uiState.value?.shouldShowSuccessAndEmpty).isEqualTo(false)
            assertThat(vm.uiState.value?.shouldShowLoading).isEqualTo(true)
            assertThat(vm.uiState.value?.shouldShowError).isEqualTo(false)
        }
        softly.assertAll()
    }

    @Test
    fun `티켓 제시를 요청하면 이벤트가 발생한다`() {
        // given

        // when
        vm.showTicketEntry(1L)

        // then
        assertThat(vm.event.getValue()).isInstanceOf(TicketListEvent.ShowTicketEntry::class.java)

        // and
        val actual = (vm.event.getValue() as TicketListEvent.ShowTicketEntry).ticketId
        val expected = 1L
        assertThat(actual).isEqualTo(expected)
    }
}
