package com.festago.acceptance.steps;

import static org.assertj.core.api.Assertions.assertThat;

import com.festago.acceptance.CucumberClient;
import com.festago.auth.dto.AdminLoginRequest;
import com.festago.auth.dto.RootAdminInitializeRequest;
import com.festago.festival.dto.FestivalCreateRequest;
import com.festago.festival.dto.FestivalDetailResponse;
import com.festago.festival.dto.FestivalResponse;
import com.festago.festival.dto.FestivalsResponse;
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
            .post("admin/initialize")
            .then().log().all()
            .statusCode(200);

        ExtractableResponse<Response> response = RestAssured.given()
            .contentType(ContentType.JSON)
            .body(new AdminLoginRequest("admin", password))
            .post("admin/login")
            .then().log().all()
            .extract();

        String token = "token";
        cucumberClient.addAuthToken(response.cookie(token));
    }

    @Given("축제를 생성하고")
    public void given() {

        FestivalCreateRequest request = new FestivalCreateRequest("푸우 축제", LocalDate.now(),
            LocalDate.now().plusDays(1), "thumnail");
        FestivalResponse response = RestAssured.given()
            .contentType(ContentType.JSON)
            .cookie("token", cucumberClient.getToken())
            .body(request)
            .post("admin/festivals")
            .then()
            .log().all()
            .extract()
            .body()
            .as(FestivalResponse.class);
        cucumberClient.addData("festivalData", response);
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
