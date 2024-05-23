package com.festago.common.aop;

import com.festago.common.exception.BadRequestException;
import com.festago.common.exception.ErrorCode;
import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Component
@Aspect
public class ValidPageableAspect {

    @Before("@annotation(ValidPageable)")
    public void doValid(JoinPoint joinPoint) {
        ValidPageable validPageable = getAnnotation(joinPoint);
        String sizeKey = validPageable.sizeKey();
        int maxSize = validPageable.maxSize();

        String sizeString = getSizeString(sizeKey);
        if (!StringUtils.hasText(sizeString)) { // 쿼리 파라미터에 size가 없을 경우 spring의 기본 값 사용
            return;
        }
        int size = parseSize(sizeString);
        validateMaxSize(size, maxSize);
    }

    private ValidPageable getAnnotation(JoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        return method.getAnnotation(ValidPageable.class);
    }

    private String getSizeString(String key) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        return request.getParameter(key);
    }

    private int parseSize(String size) {
        try {
            return Integer.parseInt(size);
        } catch (NumberFormatException e) {
            throw new BadRequestException(ErrorCode.INVALID_NUMBER_FORMAT_PAGING_SIZE);
        }
    }

    private void validateMaxSize(int size, int maxSize) {
        if (size < 1) {
            throw new BadRequestException(ErrorCode.INVALID_NUMBER_FORMAT_PAGING_SIZE);
        }
        if (maxSize < size) {
            throw new BadRequestException(ErrorCode.INVALID_PAGING_MAX_SIZE);
        }
    }
}
