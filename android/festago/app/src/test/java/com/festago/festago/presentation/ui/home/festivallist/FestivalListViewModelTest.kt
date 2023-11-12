package com.festago.festago.presentation.ui.home.festivallist

import app.cash.turbine.test
import com.festago.festago.analytics.AnalyticsHelper
import com.festago.festago.model.Festival
import com.festago.festago.model.FestivalFilter
import com.festago.festago.presentation.rule.MainDispatcherRule
import com.festago.festago.repository.FestivalRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.runTest
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.SoftAssertions
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
    val mainDispatcherRule = MainDispatcherRule()

    @Before
    fun setUp() {
        festivalRepository = mockk()
        analyticsHelper = mockk(relaxed = true)
        vm = FestivalListViewModel(festivalRepository, analyticsHelper)
    }

    private fun `축제 목록 요청 결과가 다음과 같을 때`(result: Result<List<Festival>>) {
        coEvery {
            festivalRepository.loadFestivals(any())
        } answers {
            result
        }
    }

    @Test
    fun `진행 예정인 축제 목록 받아오기에 성공하면 성공 상태이고 축제 목록을 반환하고 축제 필터가 진행 예정 필터로 바뀐다`() {
        // given
        `축제 목록 요청 결과가 다음과 같을 때`(Result.success(fakeFestivals))
        val beforeFilter = vm.festivalFilter.value

        // when
        vm.loadFestivals(FestivalFilter.PLANNED)

        // then
        val softly = SoftAssertions().apply {
            assertThat(vm.uiState.value).isInstanceOf(FestivalListUiState.Success::class.java)

            // and
            assertThat(vm.uiState.value.shouldShowSuccess).isEqualTo(true)
            assertThat(vm.uiState.value.shouldShowLoading).isEqualTo(false)
            assertThat(vm.uiState.value.shouldShowError).isEqualTo(false)

            // and
            val actual = (vm.uiState.value as FestivalListUiState.Success).festivals
            val expected = fakeFestivals.map { it.toUiState() }
            assertThat(actual).isEqualTo(expected)

            // and
            assertThat(vm.festivalFilter.value).isNotEqualTo(beforeFilter)
            assertThat(vm.festivalFilter.value).isEqualTo(FestivalFilter.PLANNED)
        }
        softly.assertAll()
    }

    @Test
    fun `진행 예정 축제 목록 받아오기에 실패하면 에러 상태이고 진행 예정 필터로 바뀐다`() {
        // given
        `축제 목록 요청 결과가 다음과 같을 때`(Result.failure(Exception()))
        val beforeFilter = vm.festivalFilter.value

        // when
        vm.loadFestivals(FestivalFilter.PLANNED)

        // then
        val softly = SoftAssertions().apply {
            assertThat(vm.uiState.value).isInstanceOf(FestivalListUiState.Error::class.java)

            // and
            assertThat(vm.uiState.value.shouldShowSuccess).isEqualTo(false)
            assertThat(vm.uiState.value.shouldShowLoading).isEqualTo(false)
            assertThat(vm.uiState.value.shouldShowError).isEqualTo(true)

            // and
            assertThat(vm.festivalFilter.value).isNotEqualTo(beforeFilter)
            assertThat(vm.festivalFilter.value).isEqualTo(FestivalFilter.PLANNED)
        }
        softly.assertAll()
    }

    @Test
    fun `축제 목록을 받아오는 중이면 로딩 상태다`() {
        // given
        coEvery {
            festivalRepository.loadFestivals(any())
        } coAnswers {
            delay(1000)
            Result.success(emptyList())
        }

        // when
        vm.loadFestivals(FestivalFilter.PLANNED)

        // then
        val softly = SoftAssertions().apply {
            assertThat(vm.uiState.value).isInstanceOf(FestivalListUiState.Loading::class.java)

            // and
            assertThat(vm.uiState.value.shouldShowSuccess).isEqualTo(false)
            assertThat(vm.uiState.value.shouldShowLoading).isEqualTo(true)
            assertThat(vm.uiState.value.shouldShowError).isEqualTo(false)
        }
        softly.assertAll()
    }

    @Test
    fun `티켓 예매를 열면 티켓 예매 열기 이벤트가 발생한다`() = runTest {
        vm.event.test {
            // when
            val fakeFestivalId = 1L
            vm.showTicketReserve(fakeFestivalId)

            // then
            assertThat(awaitItem()).isExactlyInstanceOf(FestivalListEvent.ShowTicketReserve::class.java)
        }
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
