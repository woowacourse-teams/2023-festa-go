package com.festago.acceptance;

import com.festago.support.DatabaseClearTestExecutionListener;
import com.festago.support.ResetMockTestExecutionListener;
import com.festago.support.TestTimeConfig;
import io.cucumber.java.Before;
import io.cucumber.spring.CucumberContextConfiguration;
import io.restassured.RestAssured;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestExecutionListeners;

@Import({CucumberClient.class, TestTimeConfig.class})
@CucumberContextConfiguration
@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT)
@TestExecutionListeners(listeners = {
    DatabaseClearTestExecutionListener.class,
    ResetMockTestExecutionListener.class
}, mergeMode = TestExecutionListeners.MergeMode.MERGE_WITH_DEFAULTS)
public class CucumberSpringConfiguration {

    @LocalServerPort
    private int port;

    @Before
    public void before() {
        RestAssured.port = port;
    }
}
