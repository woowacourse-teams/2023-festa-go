package com.festago.application.integration;

import com.festago.support.DatabaseClearExtension;
import com.google.firebase.messaging.FirebaseMessaging;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest(webEnvironment = WebEnvironment.NONE)
@ExtendWith(DatabaseClearExtension.class)
public abstract class ApplicationIntegrationTest {

    @MockBean
    FirebaseMessaging firebaseMessaging;
}