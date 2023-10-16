package com.festago.festago.presentation.ui.home

import app.cash.turbine.test
import com.festago.festago.repository.AuthRepository
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.assertj.core.api.Assertions.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Test

class HomeViewModelTest {
    private lateinit var vm: HomeViewModel
    private lateinit var authRepository: AuthRepository

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

    private fun `사용자 인증 유무가 다음과 같을 때`(isSigned: Boolean) {
        every { authRepository.isSigned } returns isSigned
    }

    @Test
    fun `축제 목록을 요청했을 때 토큰이 있으면 축제 목록이 보이고 이벤트가 발생하지 않는다`() = runTest {
        // given
        `사용자 인증 유무가 다음과 같을 때`(true)

        vm.event.test {
            // when
            vm.selectItem(HomeItemType.FESTIVAL_LIST)

            // then
            assertThat(vm.selectedItem.value).isEqualTo(HomeItemType.FESTIVAL_LIST)

            // and
            expectNoEvents()
        }
    }

    @Test
    fun `축제 목록을 요청했을 때 토큰이 없어도 축제 목록이 보이고 이벤트가 발생하지 않는다`() = runTest {
        // given
        `사용자 인증 유무가 다음과 같을 때`(false)

        vm.event.test {
            // when
            vm.selectItem(HomeItemType.FESTIVAL_LIST)

            // then
            assertThat(vm.selectedItem.value).isEqualTo(HomeItemType.FESTIVAL_LIST)

            // and
            expectNoEvents()
        }
    }

    @Test
    fun `티켓 목록을 요청했을 때 토큰이 있으면 티켓 목록이 보이고 이벤트가 발생하지 않는다`() = runTest {
        // given
        `사용자 인증 유무가 다음과 같을 때`(true)

        vm.event.test {
            // when
            vm.selectItem(HomeItemType.TICKET_LIST)

            // then
            assertThat(vm.selectedItem.value).isEqualTo(HomeItemType.TICKET_LIST)

            // and
            expectNoEvents()
        }
    }

    @Test
    fun `티켓 목록을 요청했을 때 토큰이 있으면 로그인 이벤트가 발생하고 선택된 화면은 축제 목록 그대로이다`() = runTest {
        // given
        `사용자 인증 유무가 다음과 같을 때`(false)

        vm.event.test {
            // when
            vm.selectItem(HomeItemType.TICKET_LIST)

            // then
            assertThat(awaitItem()).isExactlyInstanceOf(HomeEvent.ShowSignIn::class.java)

            // and
            assertThat(vm.selectedItem.value).isEqualTo(HomeItemType.FESTIVAL_LIST)
        }
    }

    @Test
    fun `마이페이지를 요청했을 때 토큰이 있으면 마이페이지가 보이고 이벤트가 발생하지 않는다`() = runTest {
        // given
        `사용자 인증 유무가 다음과 같을 때`(true)

        vm.event.test {
            // when
            vm.selectItem(HomeItemType.MY_PAGE)

            // then
            assertThat(vm.selectedItem.value).isEqualTo(HomeItemType.MY_PAGE)

            // and
            expectNoEvents()
        }
    }

    @Test
    fun `마이페이즈를 요청했을 때 토큰이 없으면 로그인 보기 이벤트가 발생하고 선택된 화면은 축제 목록 그대로이다`() = runTest {
        // given
        `사용자 인증 유무가 다음과 같을 때`(false)

        vm.event.test {
            // when
            vm.selectItem(HomeItemType.MY_PAGE)

            // then
            assertThat(awaitItem()).isExactlyInstanceOf(HomeEvent.ShowSignIn::class.java)

            // and
            assertThat(vm.selectedItem.value).isEqualTo(HomeItemType.FESTIVAL_LIST)
        }
    }
}
