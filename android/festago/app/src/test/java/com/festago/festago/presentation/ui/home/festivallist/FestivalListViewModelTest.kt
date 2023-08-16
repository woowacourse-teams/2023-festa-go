package com.festago.festago.presentation.ui.home.festivallist

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.festago.festago.analytics.AnalyticsHelper
import com.festago.festago.model.Festival
import com.festago.festago.repository.FestivalRepository
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
import java.time.LocalDate

class FestivalListViewModelTest {
    private lateinit var vm: FestivalListViewModel
    private lateinit var festivalRepository: FestivalRepository
    private lateinit var analyticsHelper: AnalyticsHelper

    private val fakeFestivals = List(5) {
        Festival(
            it.toLong(),
            "테코대학교 $it",
            LocalDate.of(2023, 5, 15),
            LocalDate.of(2023, 5, 19),
            "",
        )
    }

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setUp() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
        festivalRepository = mockk()
        analyticsHelper = mockk(relaxed = true)
        vm = FestivalListViewModel(festivalRepository, analyticsHelper)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @After
    fun finish() {
        Dispatchers.resetMain()
    }

    @Test
    fun `축제 목록 받아오기에 성공하면 성공 상태이고 축제 목록을 반환한다`() {
        // given
        coEvery {
            festivalRepository.loadFestivals()
        } answers {
            Result.success(fakeFestivals)
        }

        // when
        vm.loadFestivals()

        // then
        val softly = SoftAssertions().apply {
            assertThat(vm.uiState.value).isInstanceOf(FestivalListUiState.Success::class.java)

            // and
            assertThat(vm.uiState.value?.shouldShowSuccess).isEqualTo(true)
            assertThat(vm.uiState.value?.shouldShowLoading).isEqualTo(false)
            assertThat(vm.uiState.value?.shouldShowError).isEqualTo(false)

            // and
            val actual = (vm.uiState.value as FestivalListUiState.Success).festivals
            val expected = fakeFestivals.map { it.toUiState() }
            assertThat(actual).isEqualTo(expected)
        }
        softly.assertAll()
    }

    @Test
    fun `축제 목록 받아오기에 실패하면 에러 상태다`() {
        // given
        coEvery {
            festivalRepository.loadFestivals()
        } answers {
            Result.failure(Exception())
        }

        // when
        vm.loadFestivals()

        // then
        val softly = SoftAssertions().apply {
            assertThat(vm.uiState.value).isInstanceOf(FestivalListUiState.Error::class.java)

            // and
            assertThat(vm.uiState.value?.shouldShowSuccess).isEqualTo(false)
            assertThat(vm.uiState.value?.shouldShowLoading).isEqualTo(false)
            assertThat(vm.uiState.value?.shouldShowError).isEqualTo(true)
        }
        softly.assertAll()
    }

    @Test
    fun `축제 목록을 받아오는 중이면 로딩 상태다`() {
        // given
        coEvery {
            festivalRepository.loadFestivals()
        } coAnswers {
            delay(1000)
            Result.success(emptyList())
        }

        // when
        vm.loadFestivals()

        // then
        val softly = SoftAssertions().apply {
            assertThat(vm.uiState.value).isInstanceOf(FestivalListUiState.Loading::class.java)

            // and
            assertThat(vm.uiState.value?.shouldShowSuccess).isEqualTo(false)
            assertThat(vm.uiState.value?.shouldShowLoading).isEqualTo(true)
            assertThat(vm.uiState.value?.shouldShowError).isEqualTo(false)
        }
        softly.assertAll()
    }

    @Test
    fun `티켓 예매를 열면 티켓 예매 열기 이벤트가 발생한다`() {
        // when
        val fakeFestivalId = 1L
        vm.showTicketReserve(fakeFestivalId)

        // then
        assertThat(vm.event.getValue()).isInstanceOf(FestivalListEvent.ShowTicketReserve::class.java)
    }

    private fun Festival.toUiState() = FestivalItemUiState(
        id = id,
        name = name,
        startDate = startDate,
        endDate = endDate,
        thumbnail = thumbnail,
        onFestivalDetail = vm::showTicketReserve,
    )
}
