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
import org.assertj.core.api.SoftAssertions.assertSoftly
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

    private fun `이메일 요청 결과가 다음과 같을 때`(result: Result<String>) {
        coEvery {
            schoolRepository.loadSchoolEmail(any())
        } returns result
    }

    private fun `이메일을 불러오면`(schoolId: Long = 1L) {
        vm.loadSchoolEmail(schoolId)
    }

    private fun `인증 코드 전송 요청 결과가 다음과 같을 때`(result: Result<Unit>) {
        coEvery {
            studentVerificationRepository.sendVerificationCode(any(), any())
        } returns result
    }

    private fun `인증 코드를 전송하면`(userName: String = "test", schoolId: Long = 1) {
        vm.sendVerificationCode(userName, schoolId)
    }

    private fun `인증 코드 확인 요청 결과가 다음과 같을 떄`(result: Result<Unit>, fakeCode: String) {
        coEvery {
            studentVerificationRepository.requestVerificationCodeConfirm(
                StudentVerificationCode(fakeCode),
            )
        } returns result
    }

    private fun `인증 코드를 확인하면`() {
        vm.confirmVerificationCode()
    }

    private fun `인증 코드를 입력하면`(code: String) {
        vm.verificationCode.value = code
    }

    @Test
    fun `이메일 불러오기에 성공하면 성공 상태가 되고 이메일이 불러와진다`() {
        // given
        val fakeEmail = "test.com"

        `이메일 요청 결과가 다음과 같을 때`(Result.success(fakeEmail))

        // when
        `이메일을 불러오면`()

        // then
        assertSoftly { softly ->
            val uiState = vm.uiState.value as? StudentVerificationUiState.Success

            softly.assertThat(vm.uiState.value)
                .isExactlyInstanceOf(StudentVerificationUiState.Success::class.java)
            softly.assertThat(uiState?.schoolEmail).isEqualTo(fakeEmail)
        }
    }

    @Test
    fun `이메일 불러오기에 실패하면 실패 상태가 된다`() {
        // given
        `이메일 요청 결과가 다음과 같을 때`(Result.failure(Exception()))

        // when
        `이메일을 불러오면`()

        // then
        assertThat(vm.uiState.value).isExactlyInstanceOf(StudentVerificationUiState.Error::class.java)
    }

    @Test
    fun `이메일 불러오기에 성공한 상태에서 코드 전송에 성공하면 계속 성공 상태이고 남은 시간 초만 변경 된다`() {
        // given
        `이메일 요청 결과가 다음과 같을 때`(Result.success("test.com"))
        `인증 코드 전송 요청 결과가 다음과 같을 때`(Result.success(Unit))

        // when
        `이메일을 불러오면`()
        val beforeRemainTime = (vm.uiState.value as? StudentVerificationUiState.Success)?.remainTime

        `인증 코드를 전송하면`()

        // then
        assertSoftly { softly ->
            val uiState = vm.uiState.value as? StudentVerificationUiState.Success

            softly.assertThat(vm.uiState.value)
                .isExactlyInstanceOf(StudentVerificationUiState.Success::class.java)
            softly.assertThat(uiState?.remainTime).isNotEqualTo(beforeRemainTime)
        }
    }

    @Test
    fun `이메일을 불러오지 않은 상태에서 코드 전송에 성공해도 불러오기 중이다`() {
        // given
        `인증 코드 전송 요청 결과가 다음과 같을 때`(Result.success(Unit))

        // when
        `인증 코드를 전송하면`()

        // then
        assertThat(vm.uiState.value).isExactlyInstanceOf(StudentVerificationUiState.Loading::class.java)
    }

    @Test
    fun `이메일 불러오기 성공일 때 인증 번호가 유효하다면 인증 코드가 유효함 상태로 변경한다`() {
        // given
        `이메일 요청 결과가 다음과 같을 때`(Result.success("test.com"))

        // when
        `이메일을 불러오면`()
        `인증 코드를 입력하면`("123456")

        // then
        assertSoftly { softly ->
            val uiState = vm.uiState.value as? StudentVerificationUiState.Success

            softly.assertThat(vm.uiState.value)
                .isExactlyInstanceOf(StudentVerificationUiState.Success::class.java)
            softly.assertThat(uiState?.isValidateCode).isTrue
        }
    }

    @Test
    fun `이메일 불러오기 성공 상태일 때 인증 번호가 유효하지 않다면 인증 코드가 유효하지 않음 상태로 변경한다`() {
        // given
        `이메일 요청 결과가 다음과 같을 때`(Result.success("test.com"))

        // when
        `이메일을 불러오면`()
        `인증 코드를 입력하면`("1234")

        // then
        assertSoftly { softly ->
            val uiState = vm.uiState.value as? StudentVerificationUiState.Success

            softly.assertThat(vm.uiState.value)
                .isExactlyInstanceOf(StudentVerificationUiState.Success::class.java)
            softly.assertThat(uiState?.isValidateCode).isFalse
        }
    }

    @Test
    fun `이메일 불러오기와 인증 코드 전송이 성공일 때 인증 번호 확인이 성공하면 인증 성공 이벤트가 발생한다`() = runTest {
        // given
        val fakeCode = "123456"

        `이메일 요청 결과가 다음과 같을 때`(Result.success("test.com"))
        `인증 코드 전송 요청 결과가 다음과 같을 때`(Result.success(Unit))
        `인증 코드 확인 요청 결과가 다음과 같을 떄`(result = Result.success(Unit), fakeCode = fakeCode)

        // when
        `이메일을 불러오면`()
        `인증 코드를 입력하면`(fakeCode)
        `인증 코드를 전송하면`()

        vm.event.test {
            // when
            `인증 코드를 확인하면`()

            // then
            assertThat(awaitItem()).isEqualTo(StudentVerificationEvent.VerificationSuccess)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `이메일 불러오기와 인증 코드 전송이 성공일 때 인증 번호 확인이 실패하면 인증 실패 이벤트가 발생한다`() = runTest {
        // given
        val fakeCode = "123456"

        `이메일 요청 결과가 다음과 같을 때`(Result.success("test.com"))
        `인증 코드 전송 요청 결과가 다음과 같을 때`(Result.success(Unit))
        `인증 코드 확인 요청 결과가 다음과 같을 떄`(result = Result.failure(Exception()), fakeCode = fakeCode)

        // when
        `이메일을 불러오면`()
        `인증 코드를 입력하면`(fakeCode)
        `인증 코드를 전송하면`()

        vm.event.test {
            // when
            `인증 코드를 확인하면`()

            // then
            assertThat(awaitItem()).isExactlyInstanceOf(StudentVerificationEvent.VerificationFailure::class.java)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `이메일 불러오기 성공이고 인증 코드 전송을 하지 않았을 때 타임 아웃 이벤트가 발생한다1`() = runTest {
        // given
        val fakeCode = "123456"
        `이메일 요청 결과가 다음과 같을 때`(Result.success("test.com"))
        `인증 코드 확인 요청 결과가 다음과 같을 떄`(result = Result.failure(Exception()), fakeCode = fakeCode)

        // when
        `이메일을 불러오면`()
        `인증 코드를 입력하면`(fakeCode)

        vm.event.test {
            // when
            `인증 코드를 확인하면`()

            // then
            assertThat(awaitItem()).isEqualTo(StudentVerificationEvent.CodeTimeOut)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `이메일 불러오기 전이면 인증 코드 확인하기 요청을 보내도 이벤트가 발생하지 않는다`() = runTest {
        // given
        val fakeCode = "123456"
        `인증 코드 확인 요청 결과가 다음과 같을 떄`(result = Result.success(Unit), fakeCode = fakeCode)

        // when
        `인증 코드를 입력하면`(fakeCode)

        vm.event.test {
            // when
            `인증 코드를 확인하면`()

            // then
            expectNoEvents()
        }
    }
}
