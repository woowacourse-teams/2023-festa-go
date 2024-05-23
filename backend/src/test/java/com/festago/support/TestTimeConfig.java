package com.festago.support;

import java.time.Clock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

@TestConfiguration
public class TestTimeConfig {

    @Bean("testClock")
    @Primary
    public Clock clock() {
        return Mockito.spy(Clock.systemDefaultZone());
    }
}
