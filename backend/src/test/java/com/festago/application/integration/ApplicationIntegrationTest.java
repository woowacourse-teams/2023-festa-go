package com.festago.application.integration;

import com.festago.support.DatabaseClearExtension;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

@SpringBootTest(webEnvironment = WebEnvironment.NONE)
@ExtendWith(DatabaseClearExtension.class)
public abstract class ApplicationIntegrationTest {

}
