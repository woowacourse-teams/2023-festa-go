package com.festago.auth.domain;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Optional;

public interface TokenExtractor {

    Optional<String> extract(HttpServletRequest request);
}
