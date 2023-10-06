package com.festago.festago.presentation.ui.tickethistory

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
import org.assertj.core.api.SoftAssertions
import org.junit.After
import org.junit.Before
import org.junit.Test

class TicketHistoryViewModelTest {
    private lateinit var vm: TicketHistoryViewModel

    private lateinit var ticketRepository: TicketRepository
    private lateinit var analyticsHelper: AnalyticsHelper

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setUp() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
        ticketRepository = mockk()
        analyticsHelper = mockk(relaxed = true)

        vm = TicketHistoryViewModel(
            ticketRepository = ticketRepository,
            analyticsHelper = analyticsHelper,
        )
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @After
    fun finish() {
        Dispatchers.resetMain()
    }

    private fun `티켓 기록 요청 결과가 다음과 같을 때`(result: Result<List<Ticket>>) {
        coEvery {
            ticketRepository.loadHistoryTickets(any())
        } returns result
    }

    @Test
    fun `빈 리스트가 아닌 티켓들을 가져오면 성공 상태이다`() {
        // given
        val ids = listOf(1L, 2L, 3L, 4L, 5L)

        `티켓 기록 요청 결과가 다음과 같을 때`(Result.success(TicketFixture.getMemberTickets(ids)))

        // when
        vm.loadTicketHistories()

        // then
        val softly = SoftAssertions().apply {
            assertThat(vm.uiState.value).isInstanceOf(TicketHistoryUiState.Success::class.java)

            // and
            val successUiState = vm.uiState.value as TicketHistoryUiState.Success
            assertThat(successUiState.tickets).isEqualTo(
                TicketFixture.getMemberTickets(ids).map { it.toUiState() },
            )

            // and
            assertThat(successUiState.shouldShowSuccessWithTickets).isTrue
            assertThat(successUiState.shouldShowSuccessAndEmpty).isFalse
            assertThat(successUiState.shouldShowLoading).isFalse
            assertThat(successUiState.shouldShowError).isFalse
        }

        softly.assertAll()
    }

    @Test
    fun `빈 리스트인 티켓을 받아도 성공 상태이다`() {
        // given
        `티켓 기록 요청 결과가 다음과 같을 때`(Result.success(listOf()))

        // when
        vm.loadTicketHistories()

        // then
        val softly = SoftAssertions().apply {
            assertThat(vm.uiState.value).isInstanceOf(TicketHistoryUiState.Success::class.java)

            // and
            val successUiState = vm.uiState.value as TicketHistoryUiState.Success
            assertThat(successUiState.tickets).isEmpty()

            // and
            assertThat(successUiState.shouldShowSuccessWithTickets).isFalse
            assertThat(successUiState.shouldShowSuccessAndEmpty).isTrue
            assertThat(successUiState.shouldShowLoading).isFalse
            assertThat(successUiState.shouldShowError).isFalse
        }

        softly.assertAll()
    }

    @Test
    fun `티켓을 받아오기 전까지는 로딩 상태이다`() {
        // given
        coEvery {
            ticketRepository.loadHistoryTickets(any())
        } coAnswers {
            delay(1000)
            Result.success(emptyList())
        }

        // when
        vm.loadTicketHistories()

        // then
        val softly = SoftAssertions().apply {
            assertThat(vm.uiState.value).isInstanceOf(TicketHistoryUiState.Loading::class.java)

            // and
            assertThat(vm.uiState.value.shouldShowSuccessWithTickets).isFalse
            assertThat(vm.uiState.value.shouldShowSuccessAndEmpty).isFalse
            assertThat(vm.uiState.value.shouldShowLoading).isTrue
            assertThat(vm.uiState.value.shouldShowError).isFalse
        }

        softly.assertAll()
    }

    @Test
    fun `티켓을 받아오기 실패하면 에러 상태이다`() {
        // given
        `티켓 기록 요청 결과가 다음과 같을 때`(Result.failure(Exception()))

        // when
        vm.loadTicketHistories()

        // then
        val softly = SoftAssertions().apply {
            assertThat(vm.uiState.value).isInstanceOf(TicketHistoryUiState.Error::class.java)

            // and
            assertThat(vm.uiState.value.shouldShowSuccessWithTickets).isFalse
            assertThat(vm.uiState.value.shouldShowSuccessAndEmpty).isFalse
            assertThat(vm.uiState.value.shouldShowLoading).isFalse
            assertThat(vm.uiState.value.shouldShowError).isTrue
        }
        softly.assertAll()
    }

    private fun Ticket.toUiState(): TicketHistoryItemUiState = TicketHistoryItemUiState(
        id = id,
        number = number,
        entryTime = entryTime,
        reserveAt = reserveAt,
        stage = stage,
        festivalId = festivalTicket.id,
        festivalName = festivalTicket.name,
        festivalThumbnail = festivalTicket.thumbnail,
    )
}
