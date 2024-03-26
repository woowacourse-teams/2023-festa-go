package com.festago.support.fixture;

import java.util.concurrent.atomic.AtomicLong;
import org.springframework.util.StringUtils;

public abstract class BaseFixture {

    protected static final AtomicLong sequence = new AtomicLong();

    /**
     * value가 null 또는 빈 문자열이면 baseValue 뒤에 숫자를 붙여서 유니크한 이름을 만듭니다.
     *
     * @param defaultValue 기본 값이 될 문자열
     * @param value        null 또는 빈 문자열이 될 수 있는 이름
     * @return value가 null 또는 빈 문자열이면 defaultValue 뒤에 숫자가 붙은 문자열 그게 아니면 value 그대로 반환
     */
    protected String uniqueValue(String defaultValue, String value) {
        if (!StringUtils.hasText(value)) {
            return defaultValue + sequence.incrementAndGet();
        }
        return value;
    }
}
