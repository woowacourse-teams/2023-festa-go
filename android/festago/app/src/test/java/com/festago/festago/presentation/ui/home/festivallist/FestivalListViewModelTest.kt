package com.festago.festago.presentation.ui.home.festivallist

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.festago.festago.domain.model.Festival
import com.festago.festago.domain.repository.FestivalRepository
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
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.lang.Exception
import java.time.LocalDate

class FestivalListViewModelTest {
    private lateinit var vm: FestivalListViewModel
    private lateinit var festivalRepository: FestivalRepository

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
        vm = FestivalListViewModel(festivalRepository)
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
        assertThat(vm.uiState.value).isInstanceOf(FestivalListUiState.Success::class.java)
        assertThat(vm.uiState.value?.shouldShowSuccess).isEqualTo(true)
        assertThat(vm.uiState.value?.shouldShowLoading).isEqualTo(false)
        assertThat(vm.uiState.value?.shouldShowError).isEqualTo(false)

        val actual = (vm.uiState.value as FestivalListUiState.Success).festivals
        val expected = fakeFestivals.map { it.toPresentation() }
        assertThat(actual).isEqualTo(expected)
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
        assertThat(vm.uiState.value).isInstanceOf(FestivalListUiState.Error::class.java)
        assertThat(vm.uiState.value?.shouldShowSuccess).isEqualTo(false)
        assertThat(vm.uiState.value?.shouldShowLoading).isEqualTo(false)
        assertThat(vm.uiState.value?.shouldShowError).isEqualTo(true)
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
        assertThat(vm.uiState.value).isInstanceOf(FestivalListUiState.Loading::class.java)
        assertThat(vm.uiState.value?.shouldShowSuccess).isEqualTo(false)
        assertThat(vm.uiState.value?.shouldShowLoading).isEqualTo(true)
        assertThat(vm.uiState.value?.shouldShowError).isEqualTo(false)
    }
}
