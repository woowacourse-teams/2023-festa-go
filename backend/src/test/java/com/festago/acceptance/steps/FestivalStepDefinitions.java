package com.festago.acceptance.steps;

import static org.assertj.core.api.Assertions.assertThat;

import com.festago.acceptance.CucumberClient;
import com.festago.festival.application.FestivalService;
import com.festago.festival.dto.FestivalCreateRequest;
import com.festago.festival.dto.FestivalResponse;
import com.festago.school.repository.SchoolRepository;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

public class FestivalStepDefinitions {

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy년 MM월 dd일");
    private static final String FESTIVALS_KEY = "축제목록";

    @Autowired
    CucumberClient cucumberClient;

    @Autowired
    FestivalService festivalService;

    @Autowired
    SchoolRepository schoolRepository;

    @Given("{string}에서 시작일이 {string}, 종료일이 {string}, 이름이 {string}인 축제를 생성한다.")
    public void 축제를_생성한다(String 학교이름, String 시작일, String 종료일, String 축제이름) {
        LocalDate startDate = LocalDate.parse(시작일, DATE_TIME_FORMATTER);
        LocalDate endDate = LocalDate.parse(종료일, DATE_TIME_FORMATTER);
        Long schoolId = schoolRepository.findByName(학교이름).get().getId();
        var request = new FestivalCreateRequest(축제이름, startDate, endDate, "https://image.com/image.png", schoolId);
        festivalService.create(request);
    }

    @Then("상태가 {string}인 축제를 조회하면 {int}개의 축제가 조회된다.")
    public void 특정_상태의_축제를_조회하면_n개의_축제가_조회된다(String status, int number) {
        var festivals = getFestivalsByStatus(status);
        cucumberClient.addData("축제목록", festivals);

        assertThat(festivals).hasSize(number);
    }

    private static List<FestivalResponse> getFestivalsByStatus(String status) {
        return RestAssured.given()
            .contentType(ContentType.JSON)
            .queryParam("festivalFilter", status)
            .get("/festivals")
            .then()
            .log().ifError()
            .statusCode(200)
            .extract()
            .jsonPath()
            .getList("festivals", FestivalResponse.class);
    }

    @And("조회된 축제 중에서 {int}번째 축제의 이름은 {string} 이어야 한다.")
    public void 특정_상태인_축제를_조회할_때_n번째로_조회된_축제의_이름을_검증한다(int index, String name) {
        var festivals = (List<FestivalResponse>) cucumberClient.getData(FESTIVALS_KEY);
        Assert.notNull(festivals, "조회된 축제가 없습니다.");

        assertThat(festivals.get(index - 1).name()).isEqualTo(name);
    }
}
