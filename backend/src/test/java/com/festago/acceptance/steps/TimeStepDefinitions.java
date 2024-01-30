package com.festago.acceptance.steps;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

import com.festago.support.TimeInstantProvider;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import java.time.Clock;
import java.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;

public class TimeStepDefinitions {

    @Autowired
    Clock clock;

    @Given("현재 시간은 {int}년 {int}월 {int}일 {int}시 {int}분 이다.")
    public void 시간을_설정한다(int 년, int 월, int 일, int 시, int 분) {
        LocalDateTime localDateTime = LocalDateTime.of(년, 월, 일, 시, 분);
        given(clock.instant())
            .willReturn(TimeInstantProvider.from(localDateTime));
    }

    @Then("현재 시간은 {int}년 {int}월 {int}일 {int}시 {int}분이 되어야 한다.")
    public void 시간이_같아야_한다(int 년, int 월, int 일, int 시, int 분) {
        LocalDateTime expect = LocalDateTime.of(년, 월, 일, 시, 분);
        LocalDateTime actual = LocalDateTime.now(clock);

        assertThat(expect).isEqualTo(actual);
    }

    @Then("현재 시간은 {int}년 {int}월 {int}일 {int}시 {int}분이 아니어야 한다.")
    public void 시간은_달라야_한다(int 년, int 월, int 일, int 시, int 분) {
        LocalDateTime expect = LocalDateTime.of(년, 월, 일, 시, 분);
        LocalDateTime actual = LocalDateTime.now(clock);

        assertThat(expect).isNotEqualTo(actual);
    }
}
