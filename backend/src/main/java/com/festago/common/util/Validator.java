package com.festago.common.util;

import com.festago.common.exception.UnexpectedException;
import com.festago.common.exception.ValidException;

public final class Validator {

    private Validator() {
    }

    /**
     * 문자열이 null 또는 공백인지 검사합니다.
     *
     * @param input     검증할 문자열
     * @param fieldName 예외 메시지에 출력할 필드명
     * @throws ValidException input이 null 또는 공백이면
     */
    public static void notBlank(String input, String fieldName) {
        if (input == null || input.isBlank()) {
            throw new ValidException("%s은/는 null 또는 공백이 될 수 없습니다.".formatted(fieldName));
        }
    }

    /**
     * 문자열의 최대 길이를 검증합니다. null 값은 무시됩니다. 최대 길이가 0 이하이면 예외를 던집니다. 문자열의 길이가 maxLength 이하이면 예외를 던지지 않습니다.
     *
     * @param input     검증할 문자열
     * @param maxLength 검증할 문자열의 최대 길이
     * @param fieldName 예외 메시지에 출력할 필드명
     * @throws UnexpectedException 최대 길이가 0 이하이면
     * @throws ValidException      input의 길이가 maxLength 초과하면
     */
    public static void maxLength(CharSequence input, int maxLength, String fieldName) {
        if (maxLength <= 0) {
            throw new UnexpectedException("최대 길이는 0 이하일 수 없습니다.");
        }
        // avoid NPE
        if (input == null) {
            return;
        }
        if (input.length() > maxLength) {
            throw new ValidException("%s의 길이는 %d글자 이하여야 합니다.".formatted(fieldName, maxLength));
        }
    }

    /**
     * 문자열의 최소 길이를 검증합니다. null 값은 무시됩니다. 최소 길이가 0 이하이면 예외를 던집니다. 문자열의 길이가 minLength보다 이상이면 예외를 던지지 않습니다.
     *
     * @param input     검증할 문자열
     * @param minLength 검증할 문자열의 최소 길이
     * @param fieldName 예외 메시지에 출력할 필드명
     * @throws UnexpectedException maxLength가 0 이하이면
     * @throws ValidException      input의 길이가 minLength 미만이면
     */
    public static void minLength(CharSequence input, int minLength, String fieldName) {
        if (minLength <= 0) {
            throw new UnexpectedException("최소 길이는 0 이하일 수 없습니다.");
        }
        // avoid NPE
        if (input == null) {
            return;
        }
        if (input.length() < minLength) {
            throw new ValidException("%s의 길이는 %d글자 이상이어야 합니다.".formatted(fieldName, minLength));
        }
    }

    /**
     * 객체가 null인지 검사합니다.
     *
     * @param object    검증할 객체
     * @param fieldName 예외 메시지에 출력할 필드명
     * @throws ValidException object가 null 이면
     */
    public static void notNull(Object object, String fieldName) {
        if (object == null) {
            throw new ValidException("%s은/는 null이 될 수 없습니다.".formatted(fieldName));
        }
    }

    /**
     * 값의 최대 값을 검증합니다.
     *
     * @param value     검증할 값
     * @param maxValue  검증할 값의 최대 값
     * @param fieldName 예외 메시지에 출력할 필드명
     * @throws ValidException value가 maxValue 초과하면
     */
    public static void maxValue(int value, int maxValue, String fieldName) {
        if (value > maxValue) {
            throw new ValidException("%s은/는 %d 이하여야 합니다.".formatted(fieldName, maxValue));
        }
    }

    /**
     * 값의 최대 값을 검증합니다.
     *
     * @param value     검증할 값
     * @param maxValue  검증할 값의 최대 값
     * @param fieldName 예외 메시지에 출력할 필드명
     * @throws ValidException value가 maxValue 초과하면
     */
    public static void maxValue(long value, long maxValue, String fieldName) {
        if (value > maxValue) {
            throw new ValidException("%s은/는 %d 이하여야 합니다.".formatted(fieldName, maxValue));
        }
    }

    /**
     * 값의 최소 값을 검증합니다.
     *
     * @param value     검증할 값
     * @param minValue  검증할 값의 최소 값
     * @param fieldName 예외 메시지에 출력할 필드명
     * @throws ValidException value가 minValue 미만이면
     */
    public static void minValue(int value, int minValue, String fieldName) {
        if (value < minValue) {
            throw new ValidException("%s은/는 %d 이상이어야 합니다.".formatted(fieldName, minValue));
        }
    }

    /**
     * 값의 최소 값을 검증합니다.
     *
     * @param value     검증할 값
     * @param minValue  검증할 값의 최소 값
     * @param fieldName 예외 메시지에 출력할 필드명
     * @throws ValidException value가 minValue 미만이면
     */
    public static void minValue(long value, long minValue, String fieldName) {
        if (value < minValue) {
            throw new ValidException("%s은/는 %d 이상이어야 합니다.".formatted(fieldName, minValue));
        }
    }

    /**
     * 값이 음수인지 검증합니다.
     *
     * @param value     검증할 값
     * @param fieldName 예외 메시지에 출력할 필드명
     * @throws ValidException value가 음수이면
     */
    public static void notNegative(int value, String fieldName) {
        if (value < 0) {
            throw new ValidException("%s은/는 음수가 될 수 없습니다.".formatted(fieldName));
        }
    }

    /**
     * 값이 음수인지 검증합니다.
     *
     * @param value     검증할 값
     * @param fieldName 예외 메시지에 출력할 필드명
     * @throws ValidException value가 음수이면
     */
    public static void notNegative(long value, String fieldName) {
        if (value < 0) {
            throw new ValidException("%s은/는 음수가 될 수 없습니다.".formatted(fieldName));
        }
    }
}
