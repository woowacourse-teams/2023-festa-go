package com.festago.festago.presentation.ui.signin

import app.cash.turbine.test
import com.festago.festago.analytics.AnalyticsHelper
import com.festago.festago.presentation.rule.MainDispatcherRule
import com.festago.festago.repository.AuthRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class SignInViewModelTest {

    private lateinit var vm: SignInViewModel
    private lateinit var authRepository: AuthRepository
    private lateinit var analyticsHelper: AnalyticsHelper

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Before
    fun setUp() {
        authRepository = mockk(relaxed = true)
        analyticsHelper = mockk(relaxed = true)
        vm = SignInViewModel(authRepository, analyticsHelper)
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
