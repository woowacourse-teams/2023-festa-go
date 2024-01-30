package com.festago.acceptance.steps;

import com.festago.acceptance.CucumberClient;
import com.festago.auth.application.AdminAuthService;
import com.festago.auth.dto.AdminLoginRequest;
import io.cucumber.java.en.Given;
import org.springframework.beans.factory.annotation.Autowired;

public class AdminStepDefinitions {

    @Autowired
    CucumberClient cucumberClient;

    @Autowired
    AdminAuthService adminAuthService;

    @Given("어드민 계정으로 로그인한다.")
    public void loginAdmin() {
        adminAuthService.initializeRootAdmin("1234");
        String token = adminAuthService.login(new AdminLoginRequest("admin", "1234"));
        cucumberClient.setToken(token);
    }
}
