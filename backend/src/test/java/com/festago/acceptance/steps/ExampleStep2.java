package com.festago.acceptance.steps;

import com.festago.acceptance.CucumberClient;
import com.festago.festival.dto.FestivalDetailResponse;
import com.festago.festival.dto.FestivalResponse;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import org.springframework.beans.factory.annotation.Autowired;

public class ExampleStep2 {

    @Autowired
    CucumberClient cucumberClient;

    @When("축제를 검색하면")
    public void given() {
        FestivalResponse response = (FestivalResponse) cucumberClient.getData("festivalData");
        FestivalDetailResponse festivalInfo = RestAssured.given()
            .when()
            .get("festivals/{festivalId}", response.id())
            .then().log().all()
            .extract()
            .as(FestivalDetailResponse.class);
        cucumberClient.addData("searchResult", festivalInfo);
    }
}
