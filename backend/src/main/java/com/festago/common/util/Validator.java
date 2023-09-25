package com.festago.common.util;

public final class Validator {

    private Validator() {
    }

    /**
     * 문자열의 최대 길이를 검증합니다. null 값은 무시됩니다. 최대 길이가 0 이하이면 예외를 던집니다. 문자열의 길이가 maxLength보다 작거나 같으면 예외를 던지지 않습니다.
     *
     * @param input     검증할 문자열
     * @param maxLength 검증할 문자열의 최대 길이
     * @param message   예외 메시지
     * @throws IllegalArgumentException 문자열의 길이가 초과되거나, 최대 길이가 0 이하이면
     */
    public static void maxLength(CharSequence input, int maxLength, String message) {
        if (maxLength <= 0) {
            throw new IllegalArgumentException("검증 길이는 0보다 커야합니다.");
        }
        // avoid NPE
        if (input == null) {
            return;
        }
        if (input.length() > maxLength) {
            throw new IllegalArgumentException(message);
        }
    }

    /**
     * 문자열의 최소 길이를 검증합니다. null 값은 무시됩니다. 최소 길이가 0 이하이면 예외를 던집니다. 문자열의 길이가 minLength보다 크거나 같으면 예외를 던지지 않습니다.
     *
     * @param input     검증할 문자열
     * @param minLength 검증할 문자열의 최소 길이
     * @param message   예외 메시지
     * @throws IllegalArgumentException 문자열의 길이가 작으면, 최대 길이가 0 이하이면
     */
    public static void minLength(CharSequence input, int minLength, String message) {
        if (minLength <= 0) {
            throw new IllegalArgumentException("검증 길이는 0보다 커야합니다.");
        }
        // avoid NPE
        if (input == null) {
            return;
        }
        if (input.length() < minLength) {
            throw new IllegalArgumentException(message);
        }
    }
}
