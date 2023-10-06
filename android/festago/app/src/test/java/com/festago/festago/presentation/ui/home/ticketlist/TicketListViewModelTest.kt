package com.festago.festago.presentation.ui.home.ticketlist

import app.cash.turbine.test
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
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.assertj.core.api.SoftAssertions
import org.junit.After
import org.junit.Before
import org.junit.Test

class TicketListViewModelTest {
    private lateinit var vm: TicketListViewModel
    private lateinit var ticketRepository: TicketRepository
    private lateinit var analyticsHelper: AnalyticsHelper

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

    private fun `현재 티켓 요청 결과가 다음과 같을 때`(result: Result<List<Ticket>>) {
        coEvery { ticketRepository.loadCurrentTickets() } returns result
    }

    @Test
    fun `티켓을 받아왔을 때 티켓이 있으면 성공이고 티켓도 존재하는 상태이다`() {
        // given
        val tickets = TicketFixture.getMemberTickets((1L..10L).toList())

        `현재 티켓 요청 결과가 다음과 같을 때`(Result.success(tickets))

        // when
        vm.loadCurrentTickets()

        // then
        val softly = SoftAssertions().apply {
            assertThat(vm.uiState.value).isInstanceOf(TicketListUiState.Success::class.java)

            // and
            assertThat(vm.uiState.value.shouldShowSuccessWithTickets).isEqualTo(true)
            assertThat(vm.uiState.value.shouldShowSuccessAndEmpty).isEqualTo(false)
            assertThat(vm.uiState.value.shouldShowLoading).isEqualTo(false)
            assertThat(vm.uiState.value.shouldShowError).isEqualTo(false)

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

        `현재 티켓 요청 결과가 다음과 같을 때`(Result.success(fakeEmptyTickets))

        // when
        vm.loadCurrentTickets()

        // then
        val softly = SoftAssertions().apply {
            assertThat(vm.uiState.value).isInstanceOf(TicketListUiState.Success::class.java)

            // and
            assertThat(vm.uiState.value.shouldShowSuccessWithTickets).isEqualTo(false)
            assertThat(vm.uiState.value.shouldShowSuccessAndEmpty).isEqualTo(true)
            assertThat(vm.uiState.value.shouldShowLoading).isEqualTo(false)
            assertThat(vm.uiState.value.shouldShowError).isEqualTo(false)

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
        `현재 티켓 요청 결과가 다음과 같을 때`(Result.failure(Exception()))

        // when
        vm.loadCurrentTickets()

        // then
        val softly = SoftAssertions().apply {
            assertThat(vm.uiState.value).isInstanceOf(TicketListUiState.Error::class.java)

            // and
            assertThat(vm.uiState.value.shouldShowSuccessWithTickets).isEqualTo(false)
            assertThat(vm.uiState.value.shouldShowSuccessAndEmpty).isEqualTo(false)
            assertThat(vm.uiState.value.shouldShowLoading).isEqualTo(false)
            assertThat(vm.uiState.value.shouldShowError).isEqualTo(true)
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
            assertThat(vm.uiState.value.shouldShowSuccessWithTickets).isEqualTo(false)
            assertThat(vm.uiState.value.shouldShowSuccessAndEmpty).isEqualTo(false)
            assertThat(vm.uiState.value.shouldShowLoading).isEqualTo(true)
            assertThat(vm.uiState.value.shouldShowError).isEqualTo(false)
        }
        softly.assertAll()
    }

    @Test
    fun `1번 티켓 제시를 요청하면 1번 티켓 제시 화면 보여주기 이벤트가 발생한다`() = runTest {
        // given

        vm.event.test {
            // when
            vm.showTicketEntry(1L)

            // then
            val softly = SoftAssertions().apply {
                val event = awaitItem()
                assertThat(event).isExactlyInstanceOf(TicketListEvent.ShowTicketEntry::class.java)

                // and
                val ticketId = (event as? TicketListEvent.ShowTicketEntry)?.ticketId
                assertThat(ticketId).isEqualTo(1L)
            }
            softly.assertAll()
        }
    }
}
