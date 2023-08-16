package com.festago.festago.presentation.ui.home.mypage

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.festago.festago.analytics.AnalyticsHelper
import com.festago.festago.model.MemberTicketFestival
import com.festago.festago.model.Stage
import com.festago.festago.model.Ticket
import com.festago.festago.model.TicketCondition
import com.festago.festago.model.UserProfile
import com.festago.festago.presentation.mapper.toPresentation
import com.festago.festago.repository.AuthRepository
import com.festago.festago.repository.TicketRepository
import com.festago.festago.repository.UserRepository
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
    val instantExecutorRule = InstantTaskExecutorRule()

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setUp() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
        userRepository = mockk(relaxed = true)
        ticketRepository = mockk()
        authRepository = mockk()
        analyticsHelper = mockk(relaxed = true)
        vm = MyPageViewModel(userRepository, ticketRepository, authRepository, analyticsHelper)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @After
    fun finish() {
        Dispatchers.resetMain()
    }

    @Test
    fun `로그인 되지 않았다면 로그인 이벤트가 발생한다`() {
        // given
        coEvery {
            authRepository.isSigned
        } answers {
            false
        }

        // when
        vm.loadUserInfo()

        // then
        assertThat(vm.event.getValue() is MyPageEvent.ShowSignIn).isTrue
    }

    @Test
    fun `유저 프로필, 첫번째 티켓 받아오기에 성공하면 성공 상태다`() {
        // given
        coEvery {
            userRepository.loadUserProfile()
        } answers {
            Result.success(fakeUserProfile)
        }

        coEvery {
            ticketRepository.loadHistoryTickets(1)
        } answers {
            Result.success(fakeTickets)
        }

        coEvery {
            authRepository.isSigned
        } answers {
            true
        }

        // when
        vm.loadUserInfo()

        // then
        val softly = SoftAssertions().apply {
            assertThat(vm.uiState.value).isInstanceOf(MyPageUiState.Success::class.java)

            // and
            assertThat(vm.uiState.value?.shouldShowSuccess).isEqualTo(true)
            assertThat(vm.uiState.value?.shouldShowLoading).isEqualTo(false)
            assertThat(vm.uiState.value?.shouldShowError).isEqualTo(false)

            // and
            val actualUserProfile = (vm.uiState.value as MyPageUiState.Success).userProfile
            assertThat(actualUserProfile).isEqualTo(fakeUserProfile.toPresentation())

            // and
            val actualTicket = (vm.uiState.value as MyPageUiState.Success).ticket
            assertThat(actualTicket).isEqualTo(fakeTickets.first().toPresentation())
        }
        softly.assertAll()
    }

    @Test
    fun `유저 프로필 받아오기에 성공하고, 첫번째 티켓을 받아오는 중이면 성공 상태이고, 티켓은 없다`() {
        // given
        coEvery {
            userRepository.loadUserProfile()
        } answers {
            Result.success(fakeUserProfile)
        }

        coEvery {
            ticketRepository.loadHistoryTickets(1)
        } coAnswers {
            delay(1000)
            Result.success(fakeTickets)
        }

        coEvery {
            authRepository.isSigned
        } answers {
            true
        }

        // when
        vm.loadUserInfo()

        // then
        val softly = SoftAssertions().apply {
            assertThat(vm.uiState.value).isInstanceOf(MyPageUiState.Success::class.java)

            // and
            assertThat(vm.uiState.value?.shouldShowSuccess).isEqualTo(true)
            assertThat(vm.uiState.value?.shouldShowLoading).isEqualTo(false)
            assertThat(vm.uiState.value?.shouldShowError).isEqualTo(false)

            // and
            assertThat((vm.uiState.value as MyPageUiState.Success).hasTicket).isFalse

            // and
            val actualTicket = (vm.uiState.value as MyPageUiState.Success).ticket
            assertThat(actualTicket).isNotEqualTo(fakeTickets.first().toPresentation())
        }
        softly.assertAll()
    }

    @Test
    fun `유저 프로필 받아오기 실패하면 에러 상태다`() {
        // given
        coEvery {
            userRepository.loadUserProfile()
        } answers {
            Result.failure(Exception())
        }

        coEvery {
            ticketRepository.loadHistoryTickets(1)
        } answers {
            Result.success(fakeTickets)
        }

        coEvery {
            authRepository.isSigned
        } answers {
            true
        }

        // when
        vm.loadUserInfo()

        // then
        val softly = SoftAssertions().apply {
            assertThat(vm.uiState.value).isInstanceOf(MyPageUiState.Error::class.java)

            // and
            assertThat(vm.uiState.value?.shouldShowSuccess).isEqualTo(false)
            assertThat(vm.uiState.value?.shouldShowLoading).isEqualTo(false)
            assertThat(vm.uiState.value?.shouldShowError).isEqualTo(true)
        }
        softly.assertAll()
    }

    @Test
    fun `첫번째 티켓 받아오기에 실패하면 에러 상태다`() {
        // given
        coEvery {
            userRepository.loadUserProfile()
        } answers {
            Result.success(fakeUserProfile)
        }

        coEvery {
            ticketRepository.loadHistoryTickets(1)
        } answers {
            Result.failure(Exception())
        }

        coEvery {
            authRepository.isSigned
        } answers {
            true
        }

        // when
        vm.loadUserInfo()

        // then
        val softly = SoftAssertions().apply {
            assertThat(vm.uiState.value).isInstanceOf(MyPageUiState.Error::class.java)

            // and
            assertThat(vm.uiState.value?.shouldShowSuccess).isEqualTo(false)
            assertThat(vm.uiState.value?.shouldShowLoading).isEqualTo(false)
            assertThat(vm.uiState.value?.shouldShowError).isEqualTo(true)
        }
        softly.assertAll()
    }

    @Test
    fun `로그아웃에 성공하면 SignOutSuccess 이벤트가 발생하고 에러 상태이다`() {
        // given
        coEvery {
            authRepository.signOut()
        } answers {
            Result.success(Unit)
        }

        // when
        vm.signOut()

        // then
        val softly = SoftAssertions().apply {
            assertThat(vm.event.getValue() is MyPageEvent.SignOutSuccess).isTrue
            assertThat(vm.uiState.value is MyPageUiState.Error).isTrue

            // and
            assertThat(vm.uiState.value?.shouldShowSuccess).isEqualTo(false)
            assertThat(vm.uiState.value?.shouldShowLoading).isEqualTo(false)
            assertThat(vm.uiState.value?.shouldShowError).isEqualTo(true)
        }
        softly.assertAll()
    }

    @Test
    fun `회원탈퇴 확인 이벤트가 발생한다`() {
        // when
        vm.showConfirmDelete()

        // then
        assertThat(vm.event.getValue() is MyPageEvent.ShowConfirmDelete).isTrue
    }

    @Test
    fun `회원 탈퇴에 성공하면 DeleteAccountSuccess 이벤트가 발생하고 에러상태다`() {
        // given
        coEvery {
            authRepository.deleteAccount()
        } answers {
            Result.success(Unit)
        }

        // when
        vm.deleteAccount()

        // then
        val softly = SoftAssertions().apply {
            assertThat(vm.event.getValue() is MyPageEvent.DeleteAccountSuccess).isTrue
            assertThat(vm.uiState.value is MyPageUiState.Error).isTrue

            // and
            assertThat(vm.uiState.value?.shouldShowSuccess).isEqualTo(false)
            assertThat(vm.uiState.value?.shouldShowLoading).isEqualTo(false)
            assertThat(vm.uiState.value?.shouldShowError).isEqualTo(true)
        }
        softly.assertAll()
    }
}
