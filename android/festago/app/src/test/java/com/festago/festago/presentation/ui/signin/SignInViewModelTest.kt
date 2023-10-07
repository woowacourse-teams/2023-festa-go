package com.festago.festago.presentation.ui.signin

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.festago.festago.analytics.AnalyticsHelper
import com.festago.festago.repository.AuthRepository
import io.mockk.coEvery
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

class SignInViewModelTest {
    private lateinit var vm: SignInViewModel
    private lateinit var authRepository: AuthRepository
    private lateinit var analyticsHelper: AnalyticsHelper

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setUp() {
        Dispatchers.setMain(UnconfinedTestDispatcher())

        authRepository = mockk(relaxed = true)
        analyticsHelper = mockk(relaxed = true)
        vm = SignInViewModel(authRepository, analyticsHelper)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @After
    fun finish() {
        Dispatchers.resetMain()
    }

    @Test
    fun `로그인 성공하면 성공 이벤트가 발생한다`() {
        // given
        coEvery { authRepository.signIn(any(), any()) } answers { Result.success(Unit) }

        // when
        vm.signIn("testToken")

        // then
        assertThat(vm.event.getValue()).isExactlyInstanceOf(SignInEvent.SignInSuccess::class.java)
    }

    @Test
    fun `로그인 실패하면 실패 이벤트가 발생한다`() {
        // given
        coEvery {
            authRepository.signIn(any(), any())
        } answers { Result.failure(Exception()) }

        // when
        vm.signIn("testToken")

        // then
        assertThat(vm.event.getValue()).isExactlyInstanceOf(SignInEvent.SignInFailure::class.java)
    }

    @Test
    fun `로그인을 요청하면 로그인 화면을 보여주는 이벤트가 발생한다`() {
        // given
        // when
        vm.signInKakao()

        // then
        assertThat(vm.event.getValue()).isExactlyInstanceOf(SignInEvent.ShowSignInPage::class.java)
    }

    @Test
    fun `FCM 토큰을 불러오지 못하면 실패 이벤트가 발생한다`() {
        // given
        // when
        vm.signIn("testToken")

        // then
        assertThat(vm.event.getValue()).isExactlyInstanceOf(SignInEvent.SignInFailure::class.java)
    }
}
