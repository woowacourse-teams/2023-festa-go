package com.festago.domain;

import java.time.LocalDateTime;
import org.springframework.stereotype.Component;

@Component
public class TimeProvider {

    public LocalDateTime now() {
        return LocalDateTime.now();
    }
}
