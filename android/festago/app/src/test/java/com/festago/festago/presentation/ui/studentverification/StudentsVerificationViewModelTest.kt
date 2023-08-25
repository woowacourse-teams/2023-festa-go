package com.festago.festago.presentation.ui.studentverification

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.festago.festago.analytics.AnalyticsHelper
import com.festago.festago.presentation.ui.studentsverification.StudentVerificationUiState
import com.festago.festago.presentation.ui.studentsverification.StudentsVerificationViewModel
import com.festago.festago.repository.SchoolRepository
import com.festago.festago.repository.StudentsVerificationRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.SoftAssertions
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class StudentsVerificationViewModelTest {
    private lateinit var vm: StudentsVerificationViewModel
    private lateinit var studentsVerificationRepository: StudentsVerificationRepository
    private lateinit var schoolRepository: SchoolRepository
    private lateinit var analyticsHelper: AnalyticsHelper

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setUp() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
        studentsVerificationRepository = mockk()
        schoolRepository = mockk()
        analyticsHelper = mockk(relaxed = true)
        vm = StudentsVerificationViewModel(
            schoolRepository = schoolRepository,
            studentsVerificationRepository = studentsVerificationRepository,
            analyticsHelper = analyticsHelper,
        )
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @After
    fun finish() {
        Dispatchers.resetMain()
    }

    @Test
    fun `이메일 불러오기에 성공하면 성공 상태가 된다`() {
        // given
        val fakeEmail = "test.com"

        coEvery {
            schoolRepository.loadSchoolEmail(any())
        } returns Result.success(fakeEmail)

        // when
        vm.loadSchoolEmail(1)

        // then
        val softly = SoftAssertions().apply {
            assertThat(vm.uiState.value is StudentVerificationUiState.Success).isTrue

            // and
            val actualEmail =
                (vm.uiState.value as StudentVerificationUiState.Success).schoolEmail
            assertThat(actualEmail).isEqualTo(fakeEmail)
        }
        softly.assertAll()
    }

    @Test
    fun `이메일 불러오기에 실패하면 실패 상태가 된다`() {
        // given
        coEvery {
            schoolRepository.loadSchoolEmail(any())
        } returns Result.failure(Exception())

        // when
        vm.loadSchoolEmail(1)

        // then
        assertThat(vm.uiState.value is StudentVerificationUiState.Error).isTrue
    }

    @Test
    fun `이메일 불러오기에 성공한 상태에서 코드 전송에 성공하면 계속 성공 상태이고 남은 시간 초만 변경 된다`() {
        // given
        val fakeEmail = "test.com"

        coEvery {
            studentsVerificationRepository.sendVerificationCode(any(), any())
        } returns Result.success(Unit)

        coEvery {
            schoolRepository.loadSchoolEmail(any())
        } returns Result.success(fakeEmail)

        vm.loadSchoolEmail(1)

        val beforeRemainTime = (vm.uiState.value as StudentVerificationUiState.Success).remainTime

        // when
        vm.sendVerificationCode("test", 1)

        // then
        val softly = SoftAssertions().apply {
            assertThat(vm.uiState.value is StudentVerificationUiState.Success).isTrue

            // and
            val uiState =
                (vm.uiState.value as StudentVerificationUiState.Success)
            assertThat(uiState.remainTime).isNotEqualTo(beforeRemainTime)
            assertThat(uiState.schoolEmail).isEqualTo(fakeEmail)
        }
        softly.assertAll()
    }

    @Test
    fun `이메일 불러오기가 완료되지 않은 상태에서 코드 전송에 성공해도 로딩 상태에서 변경되지 않는다`() {
        // given
        coEvery {
            studentsVerificationRepository.sendVerificationCode(any(), any())
        } returns Result.success(Unit)

        // when
        vm.sendVerificationCode("test", 1)

        // then
        assertThat(vm.uiState.value is StudentVerificationUiState.Loading).isTrue
    }
}
