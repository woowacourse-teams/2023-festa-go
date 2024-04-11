package com.festago.acceptance.steps;

import com.festago.acceptance.CucumberClient;
import com.festago.auth.application.command.AdminAuthCommandService;
import com.festago.auth.dto.command.AdminLoginCommand;
import io.cucumber.java.en.Given;
import org.springframework.beans.factory.annotation.Autowired;

public class AdminStepDefinitions {

    @Autowired
    CucumberClient cucumberClient;

    @Autowired
    AdminAuthCommandService adminAuthCommandService;

    @Given("어드민 계정으로 로그인한다.")
    public void loginAdmin() {
        adminAuthCommandService.initializeRootAdmin("1234");
        var adminLoginResult = adminAuthCommandService.login(new AdminLoginCommand("admin", "1234"));
        cucumberClient.setToken(adminLoginResult.accessToken());
    }
}
