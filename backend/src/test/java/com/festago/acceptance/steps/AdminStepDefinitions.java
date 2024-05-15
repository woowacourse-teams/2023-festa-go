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
        AdminLoginCommand command = AdminLoginCommand.builder()
            .username("admin")
            .password("1234")
            .build();
        var adminLoginResult = adminAuthCommandService.login(command);
        cucumberClient.setToken(adminLoginResult.accessToken());
    }

    @Given("어드민 계정을 활성화한다.")
    public void initializeAdmin() {
        adminAuthCommandService.initializeRootAdmin("1234");
    }
}
