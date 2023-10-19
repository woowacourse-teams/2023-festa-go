package com.festago.festago.presentation.ui.home.festivallist

import app.cash.turbine.test
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
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.assertj.core.api.Assertions.assertThat
import org.junit.After
import org.junit.Before
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

    private fun `축제 목록 요청 결과가 다음과 같을 때`(result: Result<List<Festival>>) {
        coEvery {
            festivalRepository.loadFestivals()
        } answers {
            result
        }
    }

    @Test
    fun `축제 목록 요청 결과가 성공이라면 처음엔 로딩 화면이었다가 축제 목록을 받아온다`() = runTest {
        // given
        coEvery {
            festivalRepository.loadFestivals()
        } coAnswers {
            delay(1000)
            Result.success(fakeFestivals)
        }

        vm.uiState.test {
            // before
            assertThat(awaitItem()).isExactlyInstanceOf(FestivalListUiState.Loading::class.java)

            // when
            vm.loadFestivals()

            // after
            val nextState = awaitItem() as? FestivalListUiState.Success
            assertThat(nextState).isNotNull
            assertThat(nextState?.festivals).isEqualTo(fakeFestivals.toUiState())
        }
    }

    @Test
    fun `축제 목록 요청 결과가 실패이라면 처음엔 로딩 화면이었다가 로딩 실패 화면이 뜬다`() = runTest {
        // given
        coEvery {
            festivalRepository.loadFestivals()
        } coAnswers {
            delay(1000)
            Result.failure(Exception())
        }

        // when
        vm = FestivalListViewModel(festivalRepository, analyticsHelper)

        // then
        vm.uiState.test {
            // initial
            assertThat(awaitItem()).isExactlyInstanceOf(FestivalListUiState.Loading::class.java)
            // next
            assertThat(awaitItem()).isExactlyInstanceOf(FestivalListUiState.Error::class.java)
        }
    }

    @Test
    fun `티켓 예매를 열면 티켓 예매 열기 이벤트가 발생한다`() = runTest {
        vm = FestivalListViewModel(festivalRepository, analyticsHelper)
        vm.event.test {
            // when
            val fakeFestivalId = 1L
            vm.showTicketReserve(fakeFestivalId)

            // then
            assertThat(awaitItem()).isExactlyInstanceOf(FestivalListEvent.ShowTicketReserve::class.java)
        }
    }

    private fun List<Festival>.toUiState() = map { it.toUiState() }
    private fun Festival.toUiState() = FestivalItemUiState(
        id = id,
        name = name,
        startDate = startDate,
        endDate = endDate,
        thumbnail = thumbnail,
        onFestivalDetail = vm::showTicketReserve,
    )
}
