package com.festago.acceptance.steps;

import static org.assertj.core.api.Assertions.assertThat;

import com.festago.acceptance.CucumberClient;
import com.festago.admin.dto.AdminSchoolV1Response;
import com.festago.admin.presentation.v1.dto.SchoolV1CreateRequest;
import com.festago.admin.presentation.v1.dto.SchoolV1UpdateRequest;
import com.festago.school.domain.SchoolRegion;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;

public class AdminSchoolStepDefinitions {

    @Autowired
    CucumberClient cucumberClient;

    @Given("지역이 {string}에 있고, 이름이 {string}이고, 도메인이 {string}인 학교를 생성한다.")
    public void 학교를_생성한다(String region, String name, String domain) {
        RestAssured.given()
            .contentType(ContentType.JSON)
            .body(new SchoolV1CreateRequest(name, domain, SchoolRegion.valueOf(region)))
            .cookie("token", cucumberClient.getToken())
            .post("/admin/api/v1/schools")
            .then()
            .log().ifError()
            .statusCode(201)
            .extract()
            .header("Location");
    }

    @When("이름이 {string}인 학교의 이름을 {string}로 변경한다.")
    public void 학교의_이름을_다른_이름으로_변경한다(String srcName, String dstName) {
        var response = getSchoolResponsesByName(srcName).get(0);
        RestAssured.given()
            .contentType(ContentType.JSON)
            .body(new SchoolV1UpdateRequest(dstName, response.domain(), response.region()))
            .cookie("token", cucumberClient.getToken())
            .pathParam("id", response.id())
            .patch("/admin/api/v1/schools/{id}")
            .then()
            .log().ifError()
            .statusCode(200);
    }

    public List<AdminSchoolV1Response> getSchoolResponsesByName(String src) {
        return RestAssured.given()
            .contentType(ContentType.JSON)
            .queryParams(Map.of("searchFilter", "name", "searchKeyword", src))
            .cookie("token", cucumberClient.getToken())
            .get("/admin/api/v1/schools")
            .then()
            .log().ifError()
            .statusCode(200)
            .extract()
            .body()
            .jsonPath()
            .getList("content", AdminSchoolV1Response.class);
    }

    @When("이름이 {string}인 학교를 삭제한다.")
    public void 특정_이름의_학교를_삭제한다(String name) {
        var response = getSchoolResponsesByName(name).get(0);
        RestAssured.given()
            .contentType(ContentType.JSON)
            .cookie("token", cucumberClient.getToken())
            .pathParam("id", response.id())
            .delete("/admin/api/v1/schools/{id}")
            .then()
            .log().ifError()
            .statusCode(204);
    }

    @Then("이름에 {string}가 포함된 학교가 조회되어야 한다.")
    public void 이름이_포함된_학교를_조회한다(String name) {
        var expect = getSchoolResponsesByName(name);

        assertThat(expect).isNotEmpty();
    }

    @Then("이름에 {string}가 포함된 학교가 조회되지 않는다.")
    public void 이름이_포함된_학교가_조회되지_않는다(String name) {
        var expect = getSchoolResponsesByName(name);

        assertThat(expect).isEmpty();
    }
}
