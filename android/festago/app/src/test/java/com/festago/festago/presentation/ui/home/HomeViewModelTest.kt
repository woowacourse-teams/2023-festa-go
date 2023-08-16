package com.festago.festago.presentation.ui.home

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.festago.festago.repository.AuthRepository
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.assertj.core.api.Assertions.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class HomeViewModelTest {
    private lateinit var vm: HomeViewModel
    private lateinit var authRepository: AuthRepository

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setUp() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
        authRepository = mockk()
        vm = HomeViewModel(authRepository)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @After
    fun finish() {
        Dispatchers.resetMain()
    }

    @Test
    fun `축제 목록을 요청했을 때 토큰이 있으면 축제 목록이 보인다`() {
        // given
        every { authRepository.isSigned } returns true

        // when
        vm.loadHomeItem(HomeItemType.FESTIVAL_LIST)

        // then
        assertThat(vm.event.getValue() is HomeEvent.ShowFestivalList).isTrue
    }

    @Test
    fun `축제 목록을 요청했을 때 토큰이 없어도 축제 목록이 보인다`() {
        // given
        every { authRepository.isSigned } returns false

        // when
        vm.loadHomeItem(HomeItemType.FESTIVAL_LIST)

        // then
        assertThat(vm.event.getValue() is HomeEvent.ShowFestivalList).isTrue
    }

    @Test
    fun `티켓 목록을 요청했을 때 토큰이 있으면 티켓 목록 보기 이벤트가 발생한다`() {
        // given
        every { authRepository.isSigned } returns true

        // when
        vm.loadHomeItem(HomeItemType.TICKET_LIST)

        // then
        assertThat(vm.event.getValue() is HomeEvent.ShowTicketList).isTrue
    }

    @Test
    fun `티켓 목록을 요청했을 때 토큰이 있으면 로그인 보기 이벤트가 발생한다`() {
        // given
        every { authRepository.isSigned } returns false

        // when
        vm.loadHomeItem(HomeItemType.TICKET_LIST)

        // then
        assertThat(vm.event.getValue() is HomeEvent.ShowSignIn).isTrue
    }

    @Test
    fun `마이페이지를 요청했을 때 토큰이 있으면 마이페이지 보기 이벤트가 발생한다`() {
        // given
        every { authRepository.isSigned } returns true

        // when
        vm.loadHomeItem(HomeItemType.MY_PAGE)

        // then
        assertThat(vm.event.getValue() is HomeEvent.ShowMyPage).isTrue
    }

    @Test
    fun `마이페이즈를 요청했을 때 토큰이 없으면 로그인 보기 이벤트가 발생한다`() {
        // given
        every { authRepository.isSigned } returns false

        // when
        vm.loadHomeItem(HomeItemType.MY_PAGE)

        // then
        assertThat(vm.event.getValue() is HomeEvent.ShowSignIn).isTrue
    }
}
