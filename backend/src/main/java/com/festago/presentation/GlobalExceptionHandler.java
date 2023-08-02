package com.festago.presentation;

import com.festago.dto.ErrorResponse;
import com.festago.exception.BadRequestException;
import com.festago.exception.ErrorCode;
import com.festago.exception.FestaGoException;
import com.festago.exception.InternalServerException;
import com.festago.exception.NotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    private static final String LOG_FORMAT = "\n[🚨ERROR]\n{}: {} ({} {})\n[CALLED BY] {} \n[REQUEST BODY] \n{}";
    private static final String ERROR_LOG_FORMAT = "\n[🚨ERROR]\n{} ({} {})\n[CALLED BY] {} \n[REQUEST BODY] \n{}";
    private static final Logger errorLogger = LoggerFactory.getLogger("ErrorLogger");

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorResponse> handle(BadRequestException e, HttpServletRequest request) throws IOException {
        log(errorLogger::info, e, request);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(ErrorResponse.from(e));
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponse> handle(NotFoundException e, HttpServletRequest request) throws IOException {
        log(errorLogger::info, e, request);
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(ErrorResponse.from(e));
    }

    @ExceptionHandler(InternalServerException.class)
    public ResponseEntity<ErrorResponse> handle(InternalServerException e, HttpServletRequest request)
        throws IOException {
        log(errorLogger::warn, e, request);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(ErrorResponse.from(ErrorCode.INTERNAL_SERVER_ERROR));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handle(Exception e, HttpServletRequest request) throws IOException {
        log(errorLogger::error, e, request);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(ErrorResponse.from(ErrorCode.INTERNAL_SERVER_ERROR));
    }

    private void log(LogFunction logFunction, FestaGoException e, HttpServletRequest request) throws IOException {
        logFunction.log(
            LOG_FORMAT,
            e.getErrorCode(),
            e.getMessage(),
            request.getMethod(),
            request.getRequestURI(),
            e.getStackTrace()[0],
            getRequestPayload(request),
            e
        );
    }

    private void log(LogFunction logFunction, Exception e, HttpServletRequest request) throws IOException {
        logFunction.log(
            ERROR_LOG_FORMAT,
            e.getClass().getSimpleName(),
            request.getMethod(),
            request.getRequestURI(),
            e.getStackTrace()[0],
            getRequestPayload(request),
            e
        );
    }

    private String getRequestPayload(HttpServletRequest request) throws IOException {
        BufferedReader reader = request.getReader();
        return reader.lines().collect(Collectors.joining(System.lineSeparator()));
    }

    @FunctionalInterface
    interface LogFunction {

        void log(String format, Object... arguments);
    }
}
