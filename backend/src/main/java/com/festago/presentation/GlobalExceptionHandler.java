package com.festago.presentation;

import com.festago.common.exception.BadRequestException;
import com.festago.common.exception.ErrorCode;
import com.festago.common.exception.FestaGoException;
import com.festago.common.exception.ForbiddenException;
import com.festago.common.exception.InternalServerException;
import com.festago.common.exception.NotFoundException;
import com.festago.common.exception.TooManyRequestException;
import com.festago.common.exception.UnauthorizedException;
import com.festago.common.exception.dto.ErrorResponse;
import com.festago.presentation.auth.AuthenticateContext;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.InvalidMediaTypeException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    private static final String LOG_FORMAT_INFO = "\n[🔵INFO] - ({} {})\n(id: {}, role: {})\n{}\n {}: {}";
    private static final String LOG_FORMAT_WARN = "\n[🟠WARN] - ({} {})\n(id: {}, role: {})\n{}\n {}: {}";
    private static final String LOG_FORMAT_ERROR = "\n[🔴ERROR] - ({} {})\n(id: {}, role: {})";
    // INFO
    /*
        [🔵INFO] - (POST /admin/info)
        (id: 1, role: MEMBER)
        FOR_TEST_ERROR
         com.festago.exception.BadRequestException: 테스트용 에러입니다.
     */

    // WARN
    /*
        [🟠WARN] - (POST /admin/warn)
        (id: 1, role: MEMBER)
        FOR_TEST_ERROR
         com.festago.exception.InternalServerException: 테스트용 에러입니다.
     */

    // ERROR
    /*
        [🔴ERROR] - (POST /admin/error)
        (id: 1, role: MEMBER)
         java.lang.IllegalArgumentException: 테스트용 에러입니다.
          at com.festago.presentation.AdminController.getError(AdminController.java:129)
     */

    private final AuthenticateContext authenticateContext;
    private final Logger errorLogger;

    @ExceptionHandler(InvalidMediaTypeException.class)
    public ResponseEntity<ErrorResponse> handle(InvalidMediaTypeException e) {
        return ResponseEntity.badRequest().build();
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorResponse> handle(BadRequestException e, HttpServletRequest request) {
        logInfo(e, request);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ErrorResponse.from(e));
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ErrorResponse> handle(UnauthorizedException e, HttpServletRequest request) {
        logInfo(e, request);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ErrorResponse.from(e));
    }

    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<ErrorResponse> handle(ForbiddenException e, HttpServletRequest request) {
        logInfo(e, request);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ErrorResponse.from(e));
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponse> handle(NotFoundException e, HttpServletRequest request) {
        logInfo(e, request);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ErrorResponse.from(e));
    }

    @ExceptionHandler(TooManyRequestException.class)
    public ResponseEntity<ErrorResponse> handle(TooManyRequestException e, HttpServletRequest request) {
        logInfo(e, request);
        return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).body(ErrorResponse.from(e));
    }

    @ExceptionHandler(InternalServerException.class)
    public ResponseEntity<ErrorResponse> handle(InternalServerException e, HttpServletRequest request) {
        logWarn(e, request);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(ErrorResponse.from(ErrorCode.INTERNAL_SERVER_ERROR));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handle(Exception e, HttpServletRequest request) {
        logError(e, request);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(ErrorResponse.from(ErrorCode.INTERNAL_SERVER_ERROR));
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException e,
                                                                  HttpHeaders headers,
                                                                  HttpStatusCode status, WebRequest request) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(ErrorResponse.from(ErrorCode.INVALID_REQUEST_ARGUMENT));
    }

    private void logInfo(FestaGoException e, HttpServletRequest request) {
        if (errorLogger.isInfoEnabled()) {
            errorLogger.info(LOG_FORMAT_INFO, request.getMethod(), request.getRequestURI(), authenticateContext.getId(),
                authenticateContext.getRole(), e.getErrorCode(), e.getClass().getName(), e.getMessage());
        }
    }

    private void logWarn(FestaGoException e, HttpServletRequest request) {
        if (errorLogger.isWarnEnabled()) {
            errorLogger.warn(LOG_FORMAT_WARN, request.getMethod(), request.getRequestURI(), authenticateContext.getId(),
                authenticateContext.getRole(), e.getErrorCode(), e.getClass().getName(), e.getMessage());
        }
    }

    private void logError(Exception e, HttpServletRequest request) {
        if (errorLogger.isErrorEnabled()) {
            errorLogger.error(LOG_FORMAT_ERROR, request.getMethod(), request.getRequestURI(),
                authenticateContext.getId(), authenticateContext.getRole(), e);
        }
    }
}
