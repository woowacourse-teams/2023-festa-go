package com.festago.festago.presentation.ui.home.ticketlist

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
import java.time.LocalDateTime

class TicketListViewModelTest {
    private lateinit var vm: TicketListViewModel
    private lateinit var ticketRepository: TicketRepository

    private val fakeStage = Stage(
        1,
        "테코대학교 DAY1",
        LocalDateTime.of(2023, 7, 29, 18, 0),
    )

    private val fakeTickets = List(10) {
        Ticket(
            it.toLong(),
            100,
            LocalDateTime.of(2023, 7, 29, 15, 0),
            TicketState.BEFORE_ENTRY,
            fakeStage,
            "test.com",
        )
    }
    private val fakeEmptyTickets = emptyList<Ticket>()

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setUp() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
        ticketRepository = mockk()
        vm = TicketListViewModel(ticketRepository)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @After
    fun finish() {
        Dispatchers.resetMain()
    }

    @Test
    fun `티켓을 받아왔을 때 티켓이 있으면 성공이고 티켓도 존재하는 상태이다`() {
        // given
        coEvery { ticketRepository.loadTickets() } returns Result.success(fakeTickets)

        // when
        vm.loadTickets()

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
            val expected = fakeTickets.toPresentation()
            assertThat(actual).isEqualTo(expected)
        }
        softly.assertAll()
    }

    @Test
    fun `티켓을 받아왔을 때 티켓이 없으면 성공이지만 티켓은 없는 상태이다`() {
        // given
        coEvery { ticketRepository.loadTickets() } returns Result.success(fakeEmptyTickets)

        // when
        vm.loadTickets()

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
            val expected = fakeEmptyTickets.toPresentation()
            assertThat(actual).isEqualTo(expected)
        }
        softly.assertAll()
    }

    @Test
    fun `티켓 목록 받아오기를 실패하면 에러 상태이다`() {
        // given
        coEvery { ticketRepository.loadTickets() } answers {
            Result.failure(Exception())
        }

        // when
        vm.loadTickets()

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
        coEvery { ticketRepository.loadTickets() } coAnswers {
            delay(1000)
            Result.success(fakeTickets)
        }

        // when
        vm.loadTickets()

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
