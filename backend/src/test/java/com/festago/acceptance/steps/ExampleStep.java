package com.festago.acceptance.steps;

import static org.assertj.core.api.Assertions.assertThat;

import com.festago.acceptance.CucumberClient;
import com.festago.auth.dto.AdminLoginRequest;
import com.festago.auth.dto.RootAdminInitializeRequest;
import com.festago.festival.dto.FestivalCreateRequest;
import com.festago.festival.dto.FestivalDetailResponse;
import com.festago.festival.dto.FestivalResponse;
import com.festago.festival.dto.FestivalsResponse;
import com.festago.school.dto.SchoolCreateRequest;
import com.festago.school.dto.SchoolResponse;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;

public class ExampleStep {

    @Autowired
    CucumberClient cucumberClient;

    @Given("로그인을 한 상태에서")
    public void login() {
        String password = "password";
        RestAssured.given()
            .contentType(ContentType.JSON)
            .body(new RootAdminInitializeRequest(password))
            .post("admin/api/initialize")
            .then()
            .statusCode(200);

        ExtractableResponse<Response> response = RestAssured.given()
            .contentType(ContentType.JSON)
            .body(new AdminLoginRequest("admin", password))
            .post("admin/api/login")
            .then()
            .extract();

        cucumberClient.addAuthToken(response.cookie("token"));
    }

    @Given("축제를 생성하고")
    public void given() {
        SchoolResponse schoolResponse = (SchoolResponse) cucumberClient.getData("schoolResponse");
        FestivalCreateRequest request = new FestivalCreateRequest("푸우 축제", LocalDate.now(),
            LocalDate.now().plusDays(1), "thumnail", schoolResponse.id());
        FestivalResponse response = RestAssured.given()
            .contentType(ContentType.JSON)
            .cookie("token", cucumberClient.getToken())
            .body(request)
            .post("admin/api/festivals")
            .then()
            .extract()
            .body()
            .as(FestivalResponse.class);
        cucumberClient.addData("festivalData", response);
    }

    @Given("{string}를 생성하고")
    public void makeSchool(String schoolName) {
        SchoolCreateRequest request = new SchoolCreateRequest(schoolName, "domain.com");
        SchoolResponse response = RestAssured.given()
            .contentType(ContentType.JSON)
            .cookie("token", cucumberClient.getToken())
            .body(request)
            .post("admin/api/schools")
            .then()
            .extract()
            .body()
            .as(SchoolResponse.class);
        cucumberClient.addData("schoolResponse", response);
    }

    @Then("축제가 있다")
    public void then() {
        FestivalDetailResponse response = (FestivalDetailResponse) cucumberClient.getData("searchResult");
        assertThat(response.name()).isEqualTo("푸우 축제");
    }

    @Then("전 시나리오에서 생성된 데이터는 없어진다")
    public void exist() {
        FestivalsResponse as = RestAssured.given()
            .when()
            .get("/festivals")
            .then()
            .extract()
            .as(FestivalsResponse.class);

        assertThat(as.festivals().size()).isEqualTo(0);
    }

}
