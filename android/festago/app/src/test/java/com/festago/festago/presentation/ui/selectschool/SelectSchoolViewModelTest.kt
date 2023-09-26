package com.festago.festago.presentation.ui.selectschool

import app.cash.turbine.test
import com.festago.festago.analytics.AnalyticsHelper
import com.festago.festago.model.School
import com.festago.festago.repository.SchoolRepository
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
class SelectSchoolViewModelTest {

    private val testDispatcher = UnconfinedTestDispatcher()
    private lateinit var vm: SelectSchoolViewModel
    private lateinit var schoolRepository: SchoolRepository
    private lateinit var analyticsHelper: AnalyticsHelper

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        schoolRepository = mockk()
        analyticsHelper = mockk(relaxed = true)
        vm = SelectSchoolViewModel(
            schoolRepository = schoolRepository,
            analyticsHelper = analyticsHelper
        )
    }

    @After
    fun finish() {
        Dispatchers.resetMain()
    }

    private fun `학교 목록 불러오기 요청 결과가 다음과 같을 때 `(result: Result<List<School>>) {
        coEvery {
            schoolRepository.loadSchools()
        } returns result
    }

    @Test
    fun `학교 목록 불러오기에 성공하면 성공 상태다`() {
        // given
        `학교 목록 불러오기 요청 결과가 다음과 같을 때 `(Result.success(fakeSchools))

        // when
        vm.loadSchools()

        // then
        assertSoftly { softly ->
            softly.assertThat(vm.uiState.value)
                .isExactlyInstanceOf(SelectSchoolUiState.Success::class.java)

            val successState = vm.uiState.value as? SelectSchoolUiState.Success
            successState?.let {
                softly.assertThat(it.enableNext).isEqualTo(false)
            }
        }
    }

    @Test
    fun `학교 목록 불러오기에 실패하면 에러 상태다`() {
        // given
        `학교 목록 불러오기 요청 결과가 다음과 같을 때 `(Result.failure(Exception()))

        // when
        vm.loadSchools()

        // then
        assertThat(vm.uiState.value).isExactlyInstanceOf(SelectSchoolUiState.Error::class.java)
    }

    @Test
    fun `성공 상태에서 학교를 선택하면 선택한 학교 ID가 uistate에 설정된다`() {
        // given
        `학교 목록 불러오기 요청 결과가 다음과 같을 때 `(Result.success(fakeSchools))
        vm.loadSchools()

        // when
        vm.selectSchool(fakeSchoolId)

        // then
        val uiState = vm.uiState.value as SelectSchoolUiState.Success
        assertThat(uiState.selectedSchoolId).isEqualTo(fakeSchoolId)
    }

    @Test
    fun `에러 상태에서 학교를 선택하면 여전히 에러 상태다`() {
        // given
        `학교 목록 불러오기 요청 결과가 다음과 같을 때 `(Result.failure(Exception()))
        vm.loadSchools()

        // when
        vm.selectSchool(fakeSchoolId)

        // then
        assertThat(vm.uiState.value).isExactlyInstanceOf(SelectSchoolUiState.Error::class.java)
    }

    @Test
    fun `성공 상태이고 학교 선택하고 학생 인증 보여주기를 시도하면 학생 인증 보여주기 이벤트가 발생한다`() = runTest {
        // given
        `학교 목록 불러오기 요청 결과가 다음과 같을 때 `(Result.success(fakeSchools))
        vm.loadSchools()
        vm.selectSchool(fakeSchoolId)

        // when
        vm.event.test {
            // when
            vm.showStudentVerification()

            // then
            assertThat(awaitItem()).isExactlyInstanceOf(SelectSchoolEvent.ShowStudentVerification::class.java)
            cancelAndIgnoreRemainingEvents()
        }
    }

    private val fakeSchoolId = 1L

    private val fakeSchools = listOf(
        School(id = fakeSchoolId, domain = "scripta", name = "Charley Sullivan"),
        School(id = 8930, domain = "movet", name = "Juliette Fleming")
    )
}
