package com.festago.aop;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.festago.exception.ErrorCode;
import com.festago.exception.InternalServerException;
import com.festago.presentation.ErrorLogger;
import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Objects;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.event.Level;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.util.ContentCachingRequestWrapper;

@Component
@Aspect
public class LogRequestBodyAspect {

    private static final long MAX_CONTENT_LENGTH = 1024;

    private final ErrorLogger errorLogger;
    private final ObjectMapper objectMapper;

    public LogRequestBodyAspect(ErrorLogger errorLogger, ObjectMapper objectMapper) {
        this.errorLogger = errorLogger;
        this.objectMapper = objectMapper;
    }

    @Around("@annotation(LogRequestBody)")
    public Object handleAll(ProceedingJoinPoint pjp) throws Throwable {
        MethodSignature signature = (MethodSignature) pjp.getSignature();
        Method method = signature.getMethod();
        LogRequestBody annotation = method.getAnnotation(LogRequestBody.class);
        Level level = Level.valueOf(annotation.level());
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

        if (attributes == null) {
            return pjp.proceed();
        }
        HttpServletRequest request = attributes.getRequest();
        if (validateRequest(request)) {
            return pjp.proceed();
        }

        if (!annotation.exceptionOnly() && errorLogger.isEnabledForLevel(level)) {
            logging(level, request);
            return pjp.proceed();
        }

        if (annotation.exceptionOnly() && errorLogger.isEnabledForLevel(level)) {
            try {
                return pjp.proceed();
            } catch (Throwable e) {
                logging(level, request);
                throw e;
            }
        }

        return pjp.proceed();
    }

    private boolean validateRequest(HttpServletRequest request) {
        return !Objects.equals(request.getContentType(), MediaType.APPLICATION_JSON_VALUE)
            || request.getContentLengthLong() > MAX_CONTENT_LENGTH;
    }

    private void logging(Level level, HttpServletRequest request) {
        errorLogger.get(level)
            .log("[REQUEST BODY]\n{}", getRequestPayload(request));
    }

    private String getRequestPayload(HttpServletRequest request) {
        ContentCachingRequestWrapper cachedRequest = (ContentCachingRequestWrapper) request;
        try {
            return objectMapper.readTree(cachedRequest.getContentAsByteArray()).toPrettyString();
        } catch (IOException e) {
            throw new InternalServerException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }
}
