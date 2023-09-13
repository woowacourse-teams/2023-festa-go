package com.festago.festago.presentation.ui.studentverification

import app.cash.turbine.test
import com.festago.festago.analytics.AnalyticsHelper
import com.festago.festago.model.StudentVerificationCode
import com.festago.festago.repository.SchoolRepository
import com.festago.festago.repository.StudentVerificationRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.SoftAssertions
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class StudentVerificationViewModelTest {

    private val testDispatcher = UnconfinedTestDispatcher()
    private lateinit var vm: StudentVerificationViewModel
    private lateinit var studentVerificationRepository: StudentVerificationRepository
    private lateinit var schoolRepository: SchoolRepository
    private lateinit var analyticsHelper: AnalyticsHelper

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        studentVerificationRepository = mockk()
        schoolRepository = mockk()
        analyticsHelper = mockk(relaxed = true)
        vm = StudentVerificationViewModel(
            schoolRepository = schoolRepository,
            studentVerificationRepository = studentVerificationRepository,
            analyticsHelper = analyticsHelper,
        )
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @After
    fun finish() {
        Dispatchers.resetMain()
    }

    private fun `이메일 불러오기`(result: Result<String>, schoolId: Long = 1) {
        coEvery {
            schoolRepository.loadSchoolEmail(any())
        } returns result

        vm.loadSchoolEmail(schoolId)
    }

    private fun `인증 코드 전송하기`(result: Result<Unit>, userName: String = "test", schoolId: Long = 1) {
        coEvery {
            studentVerificationRepository.sendVerificationCode(any(), any())
        } returns result

        vm.sendVerificationCode(userName, schoolId)
    }

    private fun `인증 코드 확인하기`(result: Result<Unit>, fakeCode: String) {
        coEvery {
            studentVerificationRepository.requestVerificationCodeConfirm(
                StudentVerificationCode(fakeCode),
            )
        } returns result

        vm.confirmVerificationCode()
    }

    @Test
    fun `이메일 불러오기에 성공하면 성공 상태가 된다`() {
        val fakeEmail = "test.com"

        // when
        `이메일 불러오기`(Result.success(fakeEmail))

        // then
        val softly = SoftAssertions().apply {
            assertThat(vm.uiState.value is StudentVerificationUiState.Success).isTrue

            val actualEmail = (vm.uiState.value as StudentVerificationUiState.Success).schoolEmail
            assertThat(actualEmail).isEqualTo(fakeEmail)
        }
        softly.assertAll()
    }

    @Test
    fun `이메일 불러오기에 실패하면 실패 상태가 된다`() {
        // given
        `이메일 불러오기`(Result.failure(Exception()))

        // then
        assertThat(vm.uiState.value is StudentVerificationUiState.Error).isTrue
    }

    @Test
    fun `이메일 불러오기에 성공한 상태에서 코드 전송에 성공하면 계속 성공 상태이고 남은 시간 초만 변경 된다`() {
        // given
        `이메일 불러오기`(Result.success("test.com"))

        val beforeRemainTime = (vm.uiState.value as StudentVerificationUiState.Success).remainTime

        `인증 코드 전송하기`(Result.success(Unit))

        // then
        val softly = SoftAssertions().apply {
            assertThat(vm.uiState.value is StudentVerificationUiState.Success).isTrue

            // and
            val uiState =
                (vm.uiState.value as StudentVerificationUiState.Success)
            assertThat(uiState.remainTime).isNotEqualTo(beforeRemainTime)
        }
        softly.assertAll()
    }

    @Test
    fun `이메일 불러오기가 완료되지 않은 상태에서 코드 전송에 성공해도 불러오기 중이다`() {
        // when
        `인증 코드 전송하기`(Result.success(Unit))

        // then
        assertThat(vm.uiState.value is StudentVerificationUiState.Loading).isTrue
    }

    @Test
    fun `이메일 불러오기 성공일 때 인증 번호가 유효하다면 인증 코드가 유효함 상태로 변경한다`() {
        // given
        `이메일 불러오기`(Result.success("test.com"))

        // when
        vm.verificationCode.value = "123456"

        // then
        val softly = SoftAssertions().apply {
            assertThat(vm.uiState.value is StudentVerificationUiState.Success).isTrue

            val uiState =
                (vm.uiState.value as StudentVerificationUiState.Success)
            assertThat(uiState.isValidateCode).isTrue
        }
        softly.assertAll()
    }

    @Test
    fun `이메일 불러오기 성공 상태일 때 인증 번호가 유효하지 않다면 인증 코드가 유효하지 않음 상태로 변경한다`() {
        // given
        `이메일 불러오기`(Result.success("test.com"))

        // when
        vm.verificationCode.value = "1234"

        // then
        val softly = SoftAssertions().apply {
            assertThat(vm.uiState.value is StudentVerificationUiState.Success).isTrue

            // 유효하지 않음
            val uiState =
                (vm.uiState.value as StudentVerificationUiState.Success)
            assertThat(uiState.isValidateCode).isFalse
        }
        softly.assertAll()
    }

    @Test
    fun `이메일 불러오기 성공 상태가 아닐 때 화면 상태를 변경할 수 없다 `() {
        // when
        vm.verificationCode.value = "1234"

        // then
        assertThat(vm.uiState.value is StudentVerificationUiState.Loading).isTrue
    }

    @Test
    fun `이메일 불러오기와 인증 코드 전송이 성공일 때 인증 번호 확인이 성공하면 인증 성공 이벤트가 발생한다`() = runTest {
        // given
        `이메일 불러오기`(Result.success("test.com"))
        `인증 코드 전송하기`(Result.success(Unit))

        val fakeCode = "123456"

        vm.verificationCode.value = fakeCode

        vm.event.test {
            // when
            `인증 코드 확인하기`(result = Result.success(Unit), fakeCode = fakeCode)

            // then
            assertThat(awaitItem()).isEqualTo(StudentVerificationEvent.VerificationSuccess)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `이메일 불러오기와 인증 코드 전송이 성공일 때 인증 번호 확인이 실패하면 인증 실패 이벤트가 발생한다`() = runTest {
        // given
        `이메일 불러오기`(Result.success("test.com"))
        `인증 코드 전송하기`(Result.success(Unit))

        val fakeCode = "123456"
        vm.verificationCode.value = fakeCode

        vm.event.test {
            // when
            `인증 코드 확인하기`(result = Result.failure(Exception()), fakeCode = fakeCode)

            // then
            assertThat(awaitItem()).isEqualTo(StudentVerificationEvent.VerificationFailure)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `이메일 불러오기 성공이고 인증 코드 전송을 하지 않았을 때 타임 아웃 이벤트가 발생한다1`() = runTest {
        // given
        `이메일 불러오기`(Result.success("test.com"))

        val fakeCode = "123456"
        vm.verificationCode.value = fakeCode

        vm.event.test {
            // when
            `인증 코드 확인하기`(result = Result.success(Unit), fakeCode = fakeCode)

            // then
            assertThat(awaitItem()).isEqualTo(StudentVerificationEvent.CodeTimeOut)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `이메일 불러오기 전이면 인증 코드 확인하기 요청을 보내도 이벤트가 발생하지 않는다`() = runTest {
        // given
        val fakeCode = "123456"
        vm.verificationCode.value = fakeCode

        vm.event.test {
            // when
            `인증 코드 확인하기`(result = Result.success(Unit), fakeCode = fakeCode)

            // then
            expectNoEvents()
        }
    }
}
