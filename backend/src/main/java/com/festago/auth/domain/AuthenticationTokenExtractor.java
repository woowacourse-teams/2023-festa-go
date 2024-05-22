package com.festago.auth.domain;

import com.festago.auth.domain.authentication.Authentication;

/**
 * 문자열 형식의 토큰을 받아 Authentication을 반환하는 인터페이스 <br/> 구현체에서 반환하는 Authentication는 반드시 null이 아니여야 한다. <br/> null을 반환하는 대신
 * AnonymousAuthentication.getInstance() 반환할 것!
 */
public interface AuthenticationTokenExtractor {

    Authentication extract(String token);
}
