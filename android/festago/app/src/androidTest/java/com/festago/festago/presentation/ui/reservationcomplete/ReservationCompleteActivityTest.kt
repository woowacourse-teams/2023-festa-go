package com.festago.festago.presentation.ui.reservationcomplete

import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.festago.festago.R
import org.junit.Rule
import org.junit.Test
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class ReservationCompleteActivityTest {
    private val reservationComplete = ReservedTicketArg(1L, 123, LocalDateTime.now())

    private val intent =
        ReservationCompleteActivity.getIntent(
            context = ApplicationProvider.getApplicationContext(),
            reservationComplete = reservationComplete,
        )

    @get:Rule
    val activityRule = ActivityScenarioRule<ReservationCompleteActivity>(intent)

    @Test
    fun 예약에_성공한_메세지를_확인한다() {
        // given

        // when & then
        onView(withId(R.id.tvReservationComplete)).check(matches(withText("예매에 성공했습니다!")))
    }

    @Test
    fun 티켓_번호_문구가_표시된다() {
        // given

        // when & then
        onView(withId(R.id.tvTicketNumberPrompt)).check(matches(withText("나의 티켓 번호")))
    }

    @Test
    fun 티켓_번호가_보인다() {
        // given

        // when & then
        onView(withId(R.id.tvReservationCompleteNumber)).check(matches(withText(reservationComplete.number.toString())))
    }

    @Test
    fun 입장_가능_시간이_보인다() {
        // given
        val entryTime = reservationComplete.entryTime.format(DateTimeFormatter.ofPattern("HH:mm"))

        // when & then
        onView(withId(R.id.tvEntryTime)).check(matches(withText("[입장 가능 시간] $entryTime")))
    }

    @Test
    fun 축제_공연_날짜가_보인다() {
        // given
        val entryTime =
            reservationComplete.entryTime.format(DateTimeFormatter.ofPattern("yyyy.MM.dd"))

        // when & then
        onView(withId(R.id.tvEntryDate)).check(matches(withText(entryTime)))
    }
}
