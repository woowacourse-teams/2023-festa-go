package com.festago.festago.presentation.ui.signin

import app.cash.turbine.test
import com.festago.festago.analytics.AnalyticsHelper
import com.festago.festago.repository.AuthRepository
import io.mockk.coEvery
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

class SignInViewModelTest {
    private lateinit var vm: SignInViewModel
    private lateinit var authRepository: AuthRepository
    private lateinit var analyticsHelper: AnalyticsHelper

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

    private fun `로그인 결과가 다음과 같을 때`(result: Result<Unit>) {
        coEvery { authRepository.signIn() } returns result
    }

    @Test
    fun `로그인 성공하면 성공 이벤트가 발생한다`() = runTest {
        // given
        `로그인 결과가 다음과 같을 때`(Result.success(Unit))

        vm.event.test {
            // when
            vm.signIn()

            // then
            assertThat(awaitItem()).isExactlyInstanceOf(SignInEvent.SignInSuccess::class.java)
        }
    }

    @Test
    fun `로그인 실패하면 실패 이벤트가 발생한다`() = runTest {
        // given
        `로그인 결과가 다음과 같을 때`(Result.failure(Exception()))

        vm.event.test {
            // when
            vm.signIn()

            // then
            assertThat(awaitItem()).isExactlyInstanceOf(SignInEvent.SignInFailure::class.java)
        }
    }
}
