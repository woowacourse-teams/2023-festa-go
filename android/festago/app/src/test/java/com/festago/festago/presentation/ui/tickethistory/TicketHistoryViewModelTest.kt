package com.festago.festago.presentation.ui.tickethistory

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.festago.festago.analytics.AnalyticsHelper
import com.festago.festago.domain.model.MemberTicketFestival
import com.festago.festago.domain.model.Stage
import com.festago.festago.domain.model.Ticket
import com.festago.festago.domain.model.TicketCondition
import com.festago.festago.domain.repository.TicketRepository
import com.festago.festago.presentation.mapper.toPresentation
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
import org.junit.Rule
import org.junit.Test
import java.time.LocalDateTime

class TicketHistoryViewModelTest {
    private lateinit var vm: TicketHistoryViewModel

    private lateinit var ticketRepository: TicketRepository
    private lateinit var analyticsHelper: AnalyticsHelper

    private val fakeTickets = listOf(
        Ticket(
            id = 1,
            number = 8934,
            entryTime = LocalDateTime.now(),
            condition = TicketCondition.BEFORE_ENTRY,
            reserveAt = LocalDateTime.now(),
            stage = Stage(id = 1, startTime = LocalDateTime.now()),
            festivalTicket = MemberTicketFestival(
                id = 4663,
                name = "테코대학교 무슨 축제 DAY1",
                thumbnail = "https://images.unsplash.com/photo-1592194996308-7b43878e84a6",
            ),
        ),
    )

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

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

    @Test
    fun `빈 리스트가 아닌 티켓들을 가져오면 성공 상태이다`() {
        // given
        coEvery {
            ticketRepository.loadHistoryTickets(any())
        } answers {
            Result.success(fakeTickets)
        }

        // when
        vm.loadTicketHistories()

        // then
        val softly = SoftAssertions().apply {
            assertThat(vm.uiState.value).isInstanceOf(TicketHistoryUiState.Success::class.java)

            // and
            val successUiState = vm.uiState.value as TicketHistoryUiState.Success
            assertThat(successUiState.tickets).isEqualTo(fakeTickets.toPresentation())

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
        coEvery {
            ticketRepository.loadHistoryTickets(any())
        } answers {
            Result.success(emptyList())
        }

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
            assertThat(vm.uiState.value?.shouldShowSuccessWithTickets).isFalse
            assertThat(vm.uiState.value?.shouldShowSuccessAndEmpty).isFalse
            assertThat(vm.uiState.value?.shouldShowLoading).isTrue
            assertThat(vm.uiState.value?.shouldShowError).isFalse
        }

        softly.assertAll()
    }

    @Test
    fun `티켓을 받아오기 실패하면 에러 상태이다`() {
        // given
        coEvery {
            ticketRepository.loadHistoryTickets(any())
        } answers {
            Result.failure(Exception())
        }

        // when
        vm.loadTicketHistories()

        // then
        val softly = SoftAssertions().apply {
            assertThat(vm.uiState.value).isInstanceOf(TicketHistoryUiState.Error::class.java)

            // and
            assertThat(vm.uiState.value?.shouldShowSuccessWithTickets).isFalse
            assertThat(vm.uiState.value?.shouldShowSuccessAndEmpty).isFalse
            assertThat(vm.uiState.value?.shouldShowLoading).isFalse
            assertThat(vm.uiState.value?.shouldShowError).isTrue
        }

        softly.assertAll()
    }
}
