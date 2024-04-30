package com.festago.auth.domain;

import java.util.Date;

/**
 * nonce을 기록하고, 기록된 nonce의 TTL은 expiredAt 이후로 삭제되게 한다. <br/> nonce 값이 이미 기록된 nonce 값이면 주어지면 사용자의 토큰이 도난 당한 것으로 판단하여 예외를
 * 던져야한다.<br/>
 */
public interface OpenIdNonceValidator {

    void validate(String nonce, Date expiredAt);
}
