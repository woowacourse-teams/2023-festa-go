package com.festago.festago.presentation.ui.ticketentry

import com.festago.festago.common.analytics.AnalyticsHelper
import com.festago.festago.model.Ticket
import com.festago.festago.model.TicketCode
import com.festago.festago.presentation.fixture.TicketFixture
import com.festago.festago.presentation.rule.MainDispatcherRule
import com.festago.festago.repository.TicketRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.assertj.core.api.SoftAssertions
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class TicketEntryViewModelTest {

    private lateinit var vm: TicketEntryViewModel
    private lateinit var ticketRepository: TicketRepository
    private lateinit var analyticsHelper: AnalyticsHelper

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Before
    fun setUp() {
        ticketRepository = mockk()
        analyticsHelper = mockk(relaxed = true)
        vm = TicketEntryViewModel(ticketRepository, analyticsHelper)
    }

    private fun `티켓 요쳥 결과는 다음과 같을 때`(result: Result<Ticket>) {
        coEvery { ticketRepository.loadTicket(any()) } returns result
    }

    private fun `티켓 코드 요청 결과는 다음과 같을 때`(result: Result<TicketCode>) {
        coEvery { ticketRepository.loadTicketCode(any()) } returns result
    }

    @Test
    fun `티켓 받아오기에 성공하면 성공 상태이고 티켓 코드와 티켓을 가지고 있다`() {
        // given
        `티켓 요쳥 결과는 다음과 같을 때`(Result.success(TicketFixture.getMemberTicket()))
        `티켓 코드 요청 결과는 다음과 같을 때`(Result.success(getFakeTicketCode()))

        // when
        vm.loadTicket(1L)
        vm.loadTicketCode(1L)

        // then
        val softly = SoftAssertions().apply {
            assertThat(vm.uiState.value).isInstanceOf(TicketEntryUiState.Success::class.java)

            // and
            assertThat(vm.uiState.value.shouldShowSuccess).isEqualTo(true)
            assertThat(vm.uiState.value.shouldShowLoading).isEqualTo(false)
            assertThat(vm.uiState.value.shouldShowError).isEqualTo(false)

            // and
            val actualTicket = (vm.uiState.value as TicketEntryUiState.Success).ticket
            assertThat(actualTicket).isEqualTo(TicketFixture.getMemberTicket())
            val actualTicketCode = (vm.uiState.value as TicketEntryUiState.Success).ticketCode
            assertThat(actualTicketCode).isEqualTo(getFakeTicketCode())
        }
        softly.assertAll()
    }

    @Test
    fun `티켓 받아오기에 실패하면 에러 상태다`() {
        // given
        `티켓 요쳥 결과는 다음과 같을 때`(Result.failure(Exception()))
        `티켓 코드 요청 결과는 다음과 같을 때`(Result.success(getFakeTicketCode()))

        // when
        vm.loadTicket(1L)
        vm.loadTicketCode(1L)

        // then
        val softly = SoftAssertions().apply {
            assertThat(vm.uiState.value).isInstanceOf(TicketEntryUiState.Error::class.java)

            // and
            assertThat(vm.uiState.value.shouldShowSuccess).isEqualTo(false)
            assertThat(vm.uiState.value.shouldShowLoading).isEqualTo(false)
            assertThat(vm.uiState.value.shouldShowError).isEqualTo(true)
        }
        softly.assertAll()
    }

    @Test
    fun `티켓 코드 받아오기에 실패하면 에러 상태다`() {
        // given
        `티켓 요쳥 결과는 다음과 같을 때`(Result.success(TicketFixture.getMemberTicket()))
        `티켓 코드 요청 결과는 다음과 같을 때`(Result.failure(Exception()))

        // when
        vm.loadTicket(1L)
        vm.loadTicketCode(1L)

        // then
        val softly = SoftAssertions().apply {
            assertThat(vm.uiState.value).isInstanceOf(TicketEntryUiState.Error::class.java)

            // and
            assertThat(vm.uiState.value.shouldShowSuccess).isEqualTo(false)
            assertThat(vm.uiState.value.shouldShowLoading).isEqualTo(false)
            assertThat(vm.uiState.value.shouldShowError).isEqualTo(true)
        }
        softly.assertAll()
    }

    @Test
    fun `티켓만 받아오면 성공해도 로딩 상태다`() = runTest {
        // given
        `티켓 요쳥 결과는 다음과 같을 때`(Result.success(TicketFixture.getMemberTicket()))

        // when
        vm.loadTicket(1L)

        // then
        val softly = SoftAssertions().apply {
            assertThat(vm.uiState.value).isInstanceOf(TicketEntryUiState.Loading::class.java)

            // and
            assertThat(vm.uiState.value.shouldShowSuccess).isEqualTo(false)
            assertThat(vm.uiState.value.shouldShowLoading).isEqualTo(true)
            assertThat(vm.uiState.value.shouldShowError).isEqualTo(false)
        }
        softly.assertAll()
    }

    @Test
    fun `티켓만 받으면 실패해도 로딩 상태다`() {
        // given
        `티켓 요쳥 결과는 다음과 같을 때`(Result.failure(Exception()))

        // when
        vm.loadTicket(1L)

        // then
        val softly = SoftAssertions().apply {
            assertThat(vm.uiState.value).isInstanceOf(TicketEntryUiState.Loading::class.java)

            // and
            assertThat(vm.uiState.value.shouldShowSuccess).isEqualTo(false)
            assertThat(vm.uiState.value.shouldShowLoading).isEqualTo(true)
            assertThat(vm.uiState.value.shouldShowError).isEqualTo(false)
        }
        softly.assertAll()
    }

    @Test
    fun `티켓코드만 받아오면 결과가 성공해도 로딩 상태다`() {
        // given
        `티켓 코드 요청 결과는 다음과 같을 때`(Result.success(getFakeTicketCode()))

        // when
        vm.loadTicketCode(1L)

        // then
        val softly = SoftAssertions().apply {
            assertThat(vm.uiState.value).isInstanceOf(TicketEntryUiState.Loading::class.java)

            // and
            assertThat(vm.uiState.value.shouldShowSuccess).isEqualTo(false)
            assertThat(vm.uiState.value.shouldShowLoading).isEqualTo(true)
            assertThat(vm.uiState.value.shouldShowError).isEqualTo(false)
        }
        softly.assertAll()
    }

    @Test
    fun `티켓 코드만 받으면 결과에 실패해도 로딩 상태다`() {
        // given
        `티켓 코드 요청 결과는 다음과 같을 때`(Result.failure(Exception()))

        // when
        vm.loadTicketCode(1L)

        // then
        val softly = SoftAssertions().apply {
            assertThat(vm.uiState.value).isInstanceOf(TicketEntryUiState.Loading::class.java)

            // and
            assertThat(vm.uiState.value.shouldShowSuccess).isEqualTo(false)
            assertThat(vm.uiState.value.shouldShowLoading).isEqualTo(true)
            assertThat(vm.uiState.value.shouldShowError).isEqualTo(false)
        }
        softly.assertAll()
    }

    private fun getFakeTicketCode() = TicketCode("test.com", 30)
}
