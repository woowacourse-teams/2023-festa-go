package com.festago.presentation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.festago.dto.ErrorResponse;
import com.festago.exception.BadRequestException;
import com.festago.exception.ErrorCode;
import com.festago.exception.FestaGoException;
import com.festago.exception.InternalServerException;
import com.festago.exception.NotFoundException;
import com.festago.exception.UnauthorizedException;
import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.EnumMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.event.Level;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.web.util.ContentCachingRequestWrapper;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    private static final String LOG_FORMAT = "\n[🚨ERROR]\n{}: {} ({} {})\n[CALLED BY] {} \n[REQUEST BODY] \n{}";
    private static final String LOG_FORMAT_WITH_TRACE = "\n[🚨ERROR]\n{} ({} {})\n[CALLED BY] {} \n[REQUEST BODY] \n{}";
    private static final Logger errorLogger = LoggerFactory.getLogger("ErrorLogger");

    private final EnumMap<Level, LogFunction> logFunctions = new EnumMap<>(Level.class);
    private final ObjectMapper objectMapper;

    public GlobalExceptionHandler(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
        this.logFunctions.put(Level.INFO, errorLogger::info);
        this.logFunctions.put(Level.WARN, errorLogger::warn);
        this.logFunctions.put(Level.ERROR, errorLogger::error);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorResponse> handle(BadRequestException e, HttpServletRequest request) {
        ContentCachingRequestWrapper cachedRequest = (ContentCachingRequestWrapper) request;
        log(Level.INFO, e, cachedRequest);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ErrorResponse.from(e));
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ErrorResponse> handle(UnauthorizedException e, HttpServletRequest request) {
        ContentCachingRequestWrapper cachedRequest = (ContentCachingRequestWrapper) request;
        log(Level.INFO, e, cachedRequest);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ErrorResponse.from(e));
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponse> handle(NotFoundException e, HttpServletRequest request) {
        ContentCachingRequestWrapper cachedRequest = (ContentCachingRequestWrapper) request;
        log(Level.INFO, e, cachedRequest);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ErrorResponse.from(e));
    }

    @ExceptionHandler(InternalServerException.class)
    public ResponseEntity<ErrorResponse> handle(InternalServerException e, HttpServletRequest request) {
        ContentCachingRequestWrapper cachedRequest = (ContentCachingRequestWrapper) request;
        logWithTrace(Level.WARN, e, cachedRequest);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(ErrorResponse.from(ErrorCode.INTERNAL_SERVER_ERROR));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handle(Exception e, HttpServletRequest request) {
        ContentCachingRequestWrapper cachedRequest = (ContentCachingRequestWrapper) request;
        logWithTrace(Level.ERROR, e, cachedRequest);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(ErrorResponse.from(ErrorCode.INTERNAL_SERVER_ERROR));
    }

    private void log(Level logLevel, FestaGoException e, ContentCachingRequestWrapper request) {
        if (!errorLogger.isEnabledForLevel(logLevel)) {
            return;
        }
        logFunctions.get(logLevel)
            .log(LOG_FORMAT, e.getErrorCode(), e.getMessage(), request.getMethod(), request.getRequestURI(),
                e.getStackTrace()[0], getRequestPayload(request));
    }

    private void logWithTrace(Level logLevel, FestaGoException e, ContentCachingRequestWrapper request) {
        if (!errorLogger.isEnabledForLevel(logLevel)) {
            return;
        }
        logFunctions.get(logLevel)
            .log(LOG_FORMAT, e.getErrorCode(), e.getMessage(), request.getMethod(), request.getRequestURI(),
                getRequestPayload(request), e);
    }

    private void logWithTrace(Level logLevel, Exception e, ContentCachingRequestWrapper request) {
        if (!errorLogger.isEnabledForLevel(logLevel)) {
            return;
        }
        logFunctions.get(logLevel)
            .log(LOG_FORMAT_WITH_TRACE, e.getClass().getSimpleName(), request.getMethod(), request.getRequestURI(),
                getRequestPayload(request), e);
    }

    private String getRequestPayload(ContentCachingRequestWrapper request) {
        try {
            return objectMapper.readTree(request.getContentAsByteArray()).toPrettyString();
        } catch (IOException e) {
            throw new InternalServerException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }

    @FunctionalInterface
    interface LogFunction {

        void log(String format, Object... arguments);
    }
}
