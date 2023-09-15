package com.festago;

import com.google.firebase.messaging.FirebaseMessaging;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
class FestaGoApplicationTests {

    @MockBean
    FirebaseMessaging firebaseMessaging;

    @Test
    void contextLoads() {
    }
}
