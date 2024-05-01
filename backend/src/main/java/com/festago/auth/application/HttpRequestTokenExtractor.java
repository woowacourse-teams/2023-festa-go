package com.festago.auth.application;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Optional;

public interface HttpRequestTokenExtractor {

    Optional<String> extract(HttpServletRequest request);
}
