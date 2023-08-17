package com.festago.presentation;

import com.festago.dto.ErrorResponse;
import com.festago.exception.BadRequestException;
import com.festago.exception.ErrorCode;
import com.festago.exception.FestaGoException;
import com.festago.exception.ForbiddenException;
import com.festago.exception.InternalServerException;
import com.festago.exception.NotFoundException;
import com.festago.exception.UnauthorizedException;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.event.Level;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    private static final String LOG_FORMAT_ERROR_CODE = "\n[🚨ERROR] - ({} {})\n{}";
    private static final String LOG_FORMAT = "\n[🚨ERROR] - ({} {})";
    /*
        [🚨ERROR] - (POST /admin/warn)
        FOR_TEST_ERROR
        com.festago.exception.InternalServerException: 테스트용 에러입니다.
     */

    private final ErrorLogger errorLogger;

    public GlobalExceptionHandler(ErrorLogger errorLogger) {
        this.errorLogger = errorLogger;
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorResponse> handle(BadRequestException e, HttpServletRequest request) {
        log(Level.INFO, e, request);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ErrorResponse.from(e));
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ErrorResponse> handle(UnauthorizedException e, HttpServletRequest request) {
        log(Level.INFO, e, request);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ErrorResponse.from(e));
    }

    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<ErrorResponse> handle(ForbiddenException e, HttpServletRequest request) {
        log(Level.INFO, e, request);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ErrorResponse.from(e));
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponse> handle(NotFoundException e, HttpServletRequest request) {
        log(Level.INFO, e, request);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ErrorResponse.from(e));
    }

    @ExceptionHandler(InternalServerException.class)
    public ResponseEntity<ErrorResponse> handle(InternalServerException e, HttpServletRequest request) {
        log(Level.WARN, e, request);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(ErrorResponse.from(ErrorCode.INTERNAL_SERVER_ERROR));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handle(Exception e, HttpServletRequest request) {
        log(Level.ERROR, e, request);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(ErrorResponse.from(ErrorCode.INTERNAL_SERVER_ERROR));
    }

    private void log(Level logLevel, FestaGoException e, HttpServletRequest request) {
        if (!errorLogger.isEnabledForLevel(logLevel)) {
            return;
        }
        errorLogger.get(logLevel)
            .log(LOG_FORMAT_ERROR_CODE, request.getMethod(), request.getRequestURI(), e.getErrorCode(), e);
    }

    private void log(Level logLevel, Exception e, HttpServletRequest request) {
        if (!errorLogger.isEnabledForLevel(logLevel)) {
            return;
        }
        errorLogger.get(logLevel)
            .log(LOG_FORMAT, request.getMethod(), request.getRequestURI(), e);
    }
}
