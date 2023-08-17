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
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Scanner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.event.Level;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    private static final String LOG_FORMAT = "\n[🚨ERROR]\n{}: {} ({} {})\n[CALLED BY] {} \n[REQUEST BODY] \n{}";
    private static final String LOG_FORMAT_WITH_TRACE = "\n[🚨ERROR]\n{} ({} {})\n[CALLED BY] {} \n[REQUEST BODY] \n{}";
    private static final Logger errorLogger = LoggerFactory.getLogger("ErrorLogger");

    private static final Map<Level, LogFunction> logFunctions = Map.of(
        Level.INFO, errorLogger::info,
        Level.WARN, errorLogger::warn,
        Level.ERROR, errorLogger::error
    );

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorResponse> handle(BadRequestException e, HttpServletRequest request) throws IOException {
        log(Level.INFO, e, request);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ErrorResponse.from(e));
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ErrorResponse> handle(UnauthorizedException e, HttpServletRequest request)
        throws IOException {
        log(Level.INFO, e, request);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ErrorResponse.from(e));
    }

    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<ErrorResponse> handle(ForbiddenException e, HttpServletRequest request)
        throws IOException {
        log(Level.INFO, e, request);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ErrorResponse.from(e));
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponse> handle(NotFoundException e, HttpServletRequest request) throws IOException {
        log(Level.INFO, e, request);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ErrorResponse.from(e));
    }

    @ExceptionHandler(InternalServerException.class)
    public ResponseEntity<ErrorResponse> handle(InternalServerException e, HttpServletRequest request)
        throws IOException {
        logWithTrace(Level.WARN, e, request);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(ErrorResponse.from(ErrorCode.INTERNAL_SERVER_ERROR));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handle(Exception e, HttpServletRequest request) throws IOException {
        logWithTrace(Level.ERROR, e, request);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(ErrorResponse.from(ErrorCode.INTERNAL_SERVER_ERROR));
    }

    private void log(Level logLevel, FestaGoException e, HttpServletRequest request) throws IOException {
        if (!errorLogger.isEnabledForLevel(logLevel)) {
            return;
        }
        logFunctions.get(logLevel)
            .log(LOG_FORMAT, e.getErrorCode(), e.getMessage(), request.getMethod(), request.getRequestURI(),
                e.getStackTrace()[0], getRequestPayload(request));
    }

    private void logWithTrace(Level logLevel, FestaGoException e, HttpServletRequest request) throws IOException {
        if (!errorLogger.isEnabledForLevel(logLevel)) {
            return;
        }
        logFunctions.get(logLevel)
            .log(LOG_FORMAT, e.getErrorCode(), e.getMessage(), request.getMethod(), request.getRequestURI(),
                e.getStackTrace()[0], getRequestPayload(request), e);
    }

    private void logWithTrace(Level logLevel, Exception e, HttpServletRequest request) throws IOException {
        if (!errorLogger.isEnabledForLevel(logLevel)) {
            return;
        }
        logFunctions.get(logLevel)
            .log(LOG_FORMAT_WITH_TRACE, e.getClass().getSimpleName(), request.getMethod(), request.getRequestURI(),
                e.getStackTrace()[0], getRequestPayload(request), e);
    }

    private String getRequestPayload(HttpServletRequest request) throws IOException {
        try (InputStream inputStream = request.getInputStream(); Scanner scanner = new Scanner(inputStream,
            StandardCharsets.UTF_8)) {
            if (scanner.useDelimiter("\\A").hasNext()) {
                return scanner.next();
            } else {
                return "";
            }
        }
    }

    @FunctionalInterface
    interface LogFunction {

        void log(String format, Object... arguments);
    }
}
