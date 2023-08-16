package com.festago.festago.presentation.ui.ticketentry

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.festago.festago.analytics.AnalyticsHelper
import com.festago.festago.model.TicketCode
import com.festago.festago.presentation.fixture.TicketFixture
import com.festago.festago.presentation.mapper.toPresentation
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
import org.junit.Rule
import org.junit.Test

class TicketEntryViewModelTest {
    private lateinit var vm: TicketEntryViewModel
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
        vm = TicketEntryViewModel(ticketRepository, analyticsHelper)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @After
    fun finish() {
        Dispatchers.resetMain()
    }

    @Test
    fun `티켓 받아오기에 성공하면 성공 상태이고 티켓 코드와 티켓을 가지고 있다`() {
        // given
        coEvery {
            ticketRepository.loadTicket(any())
        } answers {
            Result.success(TicketFixture.getMemberTicket())
        }

        coEvery {
            ticketRepository.loadTicketCode(any())
        } answers {
            Result.success(getFakeTicketCode())
        }

        // when
        vm.loadTicket(1L)

        // then
        val softly = SoftAssertions().apply {
            assertThat(vm.uiState.value).isInstanceOf(TicketEntryUiState.Success::class.java)

            // and
            assertThat(vm.uiState.value?.shouldShowSuccess).isEqualTo(true)
            assertThat(vm.uiState.value?.shouldShowLoading).isEqualTo(false)
            assertThat(vm.uiState.value?.shouldShowError).isEqualTo(false)

            // and
            val actualTicket = (vm.uiState.value as TicketEntryUiState.Success).ticket
            assertThat(actualTicket).isEqualTo(TicketFixture.getMemberTicket().toPresentation())
            val actualTicketCode = (vm.uiState.value as TicketEntryUiState.Success).ticketCode
            assertThat(actualTicketCode).isEqualTo(getFakeTicketCode())
        }
        softly.assertAll()
    }

    @Test
    fun `티켓 받아오기에 실패하면 에러 상태다`() {
        // given
        coEvery {
            ticketRepository.loadTicket(any())
        } answers {
            Result.failure(Exception())
        }

        // when
        vm.loadTicket(1L)

        // then
        val softly = SoftAssertions().apply {
            assertThat(vm.uiState.value).isInstanceOf(TicketEntryUiState.Error::class.java)

            // and
            assertThat(vm.uiState.value?.shouldShowSuccess).isEqualTo(false)
            assertThat(vm.uiState.value?.shouldShowLoading).isEqualTo(false)
            assertThat(vm.uiState.value?.shouldShowError).isEqualTo(true)
        }
        softly.assertAll()
    }

    @Test
    fun `티켓 받아오는 중이면 로딩 상태다`() {
        // given
        coEvery {
            ticketRepository.loadTicket(any())
        } coAnswers {
            delay(1000)
            Result.success(TicketFixture.getMemberTicket())
        }

        // when
        vm.loadTicket(1L)

        // then
        val softly = SoftAssertions().apply {
            assertThat(vm.uiState.value).isInstanceOf(TicketEntryUiState.Loading::class.java)

            // and
            assertThat(vm.uiState.value?.shouldShowSuccess).isEqualTo(false)
            assertThat(vm.uiState.value?.shouldShowLoading).isEqualTo(true)
            assertThat(vm.uiState.value?.shouldShowError).isEqualTo(false)
        }
        softly.assertAll()
    }

    @Test
    fun `티켓 코드를 받아오기에 실패하면 에러 상태다`() {
        // given
        coEvery {
            ticketRepository.loadTicketCode(any())
        } answers {
            Result.failure(Exception())
        }

        // when
        vm.loadTicketCode(1L)

        // then
        val softly = SoftAssertions().apply {
            assertThat(vm.uiState.value).isInstanceOf(TicketEntryUiState.Error::class.java)

            // and
            assertThat(vm.uiState.value?.shouldShowSuccess).isEqualTo(false)
            assertThat(vm.uiState.value?.shouldShowLoading).isEqualTo(false)
            assertThat(vm.uiState.value?.shouldShowError).isEqualTo(true)
        }
        softly.assertAll()
    }

    private fun getFakeTicketCode() = TicketCode("test.com", 30)
}
