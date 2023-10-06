package com.festago.common.util;

import com.festago.common.exception.ValidException;

public final class Validator {

    private Validator() {
    }

    /**
     * 문자열의 최대 길이를 검증합니다. null 값은 무시됩니다. 최대 길이가 0 이하이면 예외를 던집니다. 문자열의 길이가 maxLength보다 작거나 같으면 예외를 던지지 않습니다.
     *
     * @param input     검증할 문자열
     * @param maxLength 검증할 문자열의 최대 길이
     * @param message   예외 메시지
     * @throws IllegalArgumentException 최대 길이가 0 이하이면
     * @throws ValidException           문자열의 길이가 초과되면
     */
    public static void maxLength(CharSequence input, int maxLength, String message) {
        if (maxLength <= 0) {
            throw new IllegalArgumentException("최대 길이는 0보다 커야합니다.");
        }
        // avoid NPE
        if (input == null) {
            return;
        }
        if (input.length() > maxLength) {
            throw new ValidException(message);
        }
    }

    /**
     * 문자열의 최소 길이를 검증합니다. null 값은 무시됩니다. 최소 길이가 0 이하이면 예외를 던집니다. 문자열의 길이가 minLength보다 크거나 같으면 예외를 던지지 않습니다.
     *
     * @param input     검증할 문자열
     * @param minLength 검증할 문자열의 최소 길이
     * @param message   예외 메시지
     * @throws IllegalArgumentException 최소 길이가 0 이하이면
     * @throws ValidException           문자열의 길이가 작으면
     */
    public static void minLength(CharSequence input, int minLength, String message) {
        if (minLength <= 0) {
            throw new IllegalArgumentException("최소 길이는 0보다 커야합니다.");
        }
        // avoid NPE
        if (input == null) {
            return;
        }
        if (input.length() < minLength) {
            throw new ValidException(message);
        }
    }

    /**
     * 객체가 null인지 검사합니다.
     *
     * @param object  검증할 객체
     * @param message 예외 메시지
     * @throws ValidException 객체가 null 이면
     */
    public static void notNull(Object object, String message) {
        if (object == null) {
            throw new ValidException(message);
        }
    }

    /**
     * 값의 최대 값을 검증합니다. 최대 값이 0 이하이면 예외를 던집니다.
     *
     * @param value    검증할 값
     * @param maxValue 검증할 값의 최대 값
     * @param message  예외 메시지
     * @throws ValidException           검증할 값이 최대 값보다 크면
     * @throws IllegalArgumentException 최대 값이 0 이하이면
     */
    public static void maxValue(int value, int maxValue, String message) {
        if (maxValue <= 0) {
            throw new IllegalArgumentException("최대 값은 0보다 커야합니다.");
        }
        if (value > maxValue) {
            throw new ValidException(message);
        }
    }

    /**
     * 값의 최대 값을 검증합니다. 최대 값이 0 이하이면 예외를 던집니다.
     *
     * @param value    검증할 값
     * @param maxValue 검증할 값의 최대 값
     * @param message  예외 메시지
     * @throws ValidException           검증할 값이 최대 값보다 크면
     * @throws IllegalArgumentException 최대 값이 0 이하이면
     */
    public static void maxValue(long value, int maxValue, String message) {
        if (maxValue <= 0) {
            throw new IllegalArgumentException("최대 값은 0보다 커야합니다.");
        }
        if (value > maxValue) {
            throw new ValidException(message);
        }
    }

    /**
     * 값의 최소 값을 검증합니다. 최소 값이 0 이하이면 예외를 던집니다.
     *
     * @param value    검증할 값
     * @param minValue 검증할 값의 최소 값
     * @param message  예외 메시지
     * @throws ValidException           검증할 값이 최소 값보다 작으면
     * @throws IllegalArgumentException 최소 값이 0 이하이면
     */
    public static void minValue(int value, int minValue, String message) {
        if (minValue <= 0) {
            throw new IllegalArgumentException("최소 값은 0보다 커야합니다.");
        }
        if (value < minValue) {
            throw new ValidException(message);
        }
    }

    /**
     * 값의 최소 값을 검증합니다. 최소 값이 0 이하이면 예외를 던집니다.
     *
     * @param value    검증할 값
     * @param minValue 검증할 값의 최소 값
     * @param message  예외 메시지
     * @throws ValidException           검증할 값이 최소 값보다 작으면
     * @throws IllegalArgumentException 최소 값이 0 이하이면
     */
    public static void minValue(long value, int minValue, String message) {
        if (minValue <= 0) {
            throw new IllegalArgumentException("최소 값은 0보다 커야합니다.");
        }
        if (value < minValue) {
            throw new ValidException(message);
        }
    }
}
