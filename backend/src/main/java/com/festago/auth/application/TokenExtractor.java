package com.festago.auth.application;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Optional;

public interface TokenExtractor {

    Optional<String> extract(HttpServletRequest request);
}
