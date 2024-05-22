package com.festago.auth.infrastructure;

import com.festago.auth.domain.authentication.Authentication;
import io.jsonwebtoken.Claims;

/**
 * AuthenticationTokenExtractor의 필드로 사용되기 위해 설계되었음 <br/> JWT Claims에서 값을 추출하여 Authentication을 반환하는 인터페이스 <br/> 구현체에서
 * 반환하는 Authentication는 반드시 null이 아니여야 한다. <br/> null을 반환하는 대신 AnonymousAuthentication.getInstance() 반환할 것!
 */
public interface AuthenticationClaimsExtractor {

    Authentication extract(Claims claims);
}
