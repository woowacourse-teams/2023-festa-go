package com.festago.festagostaff.presentation.ui.main

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class MainViewModelTest {
    private lateinit var vm: MainViewModel

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        vm = MainViewModel()
    }

    @Test
    fun `티켓 검증을 시작하면 티켓 검증 이벤트가 발생한다`() {
        // when
        vm.openTicketValidation()

        // then
        assertThat(vm.event.getValue()).isInstanceOf(MainEvent.OpenTicketValidation::class.java)
    }
}
