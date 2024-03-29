package com.festago.festago.presentation.ui.home.mypage

import app.cash.turbine.test
import com.festago.festago.common.analytics.AnalyticsHelper
import com.festago.festago.model.MemberTicketFestival
import com.festago.festago.model.Stage
import com.festago.festago.model.Ticket
import com.festago.festago.model.TicketCondition
import com.festago.festago.model.UserProfile
import com.festago.festago.presentation.rule.MainDispatcherRule
import com.festago.festago.repository.AuthRepository
import com.festago.festago.repository.TicketRepository
import com.festago.festago.repository.UserRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.runTest
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.SoftAssertions
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.time.LocalDateTime

class MyPageViewModelTest {

    private lateinit var vm: MyPageViewModel
    private lateinit var userRepository: UserRepository
    private lateinit var ticketRepository: TicketRepository
    private lateinit var authRepository: AuthRepository
    private lateinit var analyticsHelper: AnalyticsHelper

    private val fakeUserProfile = UserProfile(
        1L,
        "홍길동",
        "https://images.unsplash.com/photo-1592194996308-7b43878e84a6",
    )

    private val fakeTickets = listOf(
        Ticket(
            id = 1,
            number = 8934,
            entryTime = LocalDateTime.now(),
            condition = TicketCondition.BEFORE_ENTRY,
            reserveAt = LocalDateTime.now(),
            stage = Stage(id = 1, startTime = LocalDateTime.now()),
            festivalTicket = MemberTicketFestival(
                id = 4663,
                name = "테코대학교 무슨 축제 DAY1",
                thumbnail = "https://images.unsplash.com/photo-1592194996308-7b43878e84a6",
            ),
        ),
    )

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Before
    fun setUp() {
        userRepository = mockk(relaxed = true)
        ticketRepository = mockk()
        authRepository = mockk()
        analyticsHelper = mockk(relaxed = true)
        vm = MyPageViewModel(userRepository, ticketRepository, authRepository, analyticsHelper)
    }

    private fun `로그인 상태가 다음과 같다`(result: Boolean) {
        coEvery {
            authRepository.isSigned
        } answers {
            result
        }
    }

    private fun `유저 프로필 요청의 결과가 다음과 같다`(result: Result<UserProfile>) {
        coEvery {
            userRepository.loadUserProfile()
        } answers {
            result
        }
    }

    private fun `과거 예매 내역 요청의 결과가 다음과 같다`(result: Result<List<Ticket>>) {
        coEvery {
            ticketRepository.loadHistoryTickets(any())
        } answers {
            result
        }
    }

    @Test
    fun `로그인 되지 않았다면 로그인 이벤트가 발생한다`() = runTest {
        // given
        `로그인 상태가 다음과 같다`(false)

        vm.event.test {
            // when
            vm.loadUserInfo()

            // then
            assertThat(awaitItem()).isExactlyInstanceOf(MyPageEvent.ShowSignIn::class.java)
        }
    }

    @Test
    fun `유저 프로필, 과거 예매 내역 받아오기에 성공하면 성공 상태다`() {
        // given
        `유저 프로필 요청의 결과가 다음과 같다`(Result.success(fakeUserProfile))

        `과거 예매 내역 요청의 결과가 다음과 같다`(Result.success(fakeTickets))

        `로그인 상태가 다음과 같다`(true)

        // when
        vm.loadUserInfo()

        // then
        val softly = SoftAssertions().apply {
            assertThat(vm.uiState.value).isInstanceOf(MyPageUiState.Success::class.java)

            // and
            assertThat(vm.uiState.value.shouldShowSuccess).isEqualTo(true)
            assertThat(vm.uiState.value.shouldShowLoading).isEqualTo(false)
            assertThat(vm.uiState.value.shouldShowError).isEqualTo(false)

            // and
            val actualUserProfile = (vm.uiState.value as MyPageUiState.Success).userProfile
            assertThat(actualUserProfile).isEqualTo(fakeUserProfile)

            // and
            val actualTicket = (vm.uiState.value as MyPageUiState.Success).ticket
            assertThat(actualTicket).isEqualTo(fakeTickets.first())
        }
        softly.assertAll()
    }

    @Test
    fun `유저 프로필 받아오기에 성공하고, 과거 예매 내역을 받아오는 중이면 로딩 상태이다`() {
        // given
        `유저 프로필 요청의 결과가 다음과 같다`(Result.success(fakeUserProfile))

        coEvery {
            ticketRepository.loadHistoryTickets(1)
        } coAnswers {
            delay(1000)
            Result.success(fakeTickets)
        }

        `로그인 상태가 다음과 같다`(true)

        // when
        vm.loadUserInfo()

        // then
        val softly = SoftAssertions().apply {
            assertThat(vm.uiState.value).isInstanceOf(MyPageUiState.Loading::class.java)

            // and
            assertThat(vm.uiState.value.shouldShowSuccess).isEqualTo(false)
            assertThat(vm.uiState.value.shouldShowLoading).isEqualTo(true)
            assertThat(vm.uiState.value.shouldShowError).isEqualTo(false)
        }
        softly.assertAll()
    }

    @Test
    fun `유저 프로필 받아오기 실패하면 에러 상태다`() {
        // given
        `유저 프로필 요청의 결과가 다음과 같다`(Result.failure(Exception()))

        `과거 예매 내역 요청의 결과가 다음과 같다`(Result.success(fakeTickets))

        `로그인 상태가 다음과 같다`(true)

        // when
        vm.loadUserInfo()

        // then
        val softly = SoftAssertions().apply {
            assertThat(vm.uiState.value).isInstanceOf(MyPageUiState.Error::class.java)

            // and
            assertThat(vm.uiState.value.shouldShowSuccess).isEqualTo(false)
            assertThat(vm.uiState.value.shouldShowLoading).isEqualTo(false)
            assertThat(vm.uiState.value.shouldShowError).isEqualTo(true)
        }
        softly.assertAll()
    }

    @Test
    fun `과거 예매 내역 받아오기에 실패하면 에러 상태다`() {
        // given
        `유저 프로필 요청의 결과가 다음과 같다`(Result.success(fakeUserProfile))

        `과거 예매 내역 요청의 결과가 다음과 같다`(Result.failure(Exception()))

        `로그인 상태가 다음과 같다`(true)

        // when
        vm.loadUserInfo()

        // then
        val softly = SoftAssertions().apply {
            assertThat(vm.uiState.value).isInstanceOf(MyPageUiState.Error::class.java)

            // and
            assertThat(vm.uiState.value.shouldShowSuccess).isEqualTo(false)
            assertThat(vm.uiState.value.shouldShowLoading).isEqualTo(false)
            assertThat(vm.uiState.value.shouldShowError).isEqualTo(true)
        }
        softly.assertAll()
    }

    @Test
    fun `로그아웃에 성공하면 SignOutSuccess 이벤트가 발생하고 에러 상태이다`() = runTest {
        // given
        coEvery {
            authRepository.signOut()
        } answers {
            Result.success(Unit)
        }

        vm.event.test {
            // when
            vm.signOut()

            // then
            val softly = SoftAssertions().apply {
                assertThat(awaitItem()).isExactlyInstanceOf(MyPageEvent.SignOutSuccess::class.java)
                assertThat(vm.uiState.value is MyPageUiState.Error).isTrue

                // and
                assertThat(vm.uiState.value.shouldShowSuccess).isEqualTo(false)
                assertThat(vm.uiState.value.shouldShowLoading).isEqualTo(false)
                assertThat(vm.uiState.value.shouldShowError).isEqualTo(true)
            }
            softly.assertAll()
        }
    }

    @Test
    fun `회원탈퇴 확인 이벤트가 발생한다`() = runTest {
        vm.event.test {
            // when
            vm.showConfirmDelete()

            // then
            assertThat(awaitItem()).isExactlyInstanceOf(MyPageEvent.ShowConfirmDelete::class.java)
        }
    }

    @Test
    fun `회원 탈퇴에 성공하면 DeleteAccountSuccess 이벤트가 발생하고 에러상태다`() = runTest {
        // given
        coEvery {
            authRepository.deleteAccount()
        } answers {
            Result.success(Unit)
        }

        vm.event.test {
            // when
            vm.deleteAccount()

            // then
            val softly = SoftAssertions().apply {
                assertThat(awaitItem()).isExactlyInstanceOf(MyPageEvent.DeleteAccountSuccess::class.java)
                assertThat(vm.uiState.value is MyPageUiState.Error).isTrue

                // and
                assertThat(vm.uiState.value.shouldShowSuccess).isEqualTo(false)
                assertThat(vm.uiState.value.shouldShowLoading).isEqualTo(false)
                assertThat(vm.uiState.value.shouldShowError).isEqualTo(true)
            }
            softly.assertAll()
        }
    }
}
