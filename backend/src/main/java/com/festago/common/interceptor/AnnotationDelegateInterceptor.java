package com.festago.common.interceptor;

import com.festago.common.exception.UnexpectedException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.lang.annotation.Annotation;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

public class AnnotationDelegateInterceptor implements HandlerInterceptor {

    private final Class<? extends Annotation> annotation;
    private final HandlerInterceptor interceptor;

    protected AnnotationDelegateInterceptor(
        Class<? extends Annotation> annotation,
        HandlerInterceptor interceptor
    ) {
        if (annotation == null) {
            throw new UnexpectedException("annotation은 null이 될 수 없습니다.");
        }
        if (interceptor == null) {
            throw new UnexpectedException("interceptor는 null이 될 수 없습니다.");
        }
        this.annotation = annotation;
        this.interceptor = interceptor;
    }

    public static AnnotationsDelegateInterceptorBuilder builder() {
        return new AnnotationsDelegateInterceptorBuilder();
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
        throws Exception {
        if (handler instanceof HandlerMethod handlerMethod && handlerMethod.hasMethodAnnotation(annotation)) {
            return interceptor.preHandle(request, response, handler);
        }
        return true;
    }

    public static class AnnotationsDelegateInterceptorBuilder {

        private Class<? extends Annotation> annotation;
        private HandlerInterceptor interceptor;

        public AnnotationsDelegateInterceptorBuilder annotation(Class<? extends Annotation> annotation) {
            this.annotation = annotation;
            return this;
        }

        public AnnotationsDelegateInterceptorBuilder interceptor(HandlerInterceptor interceptor) {
            this.interceptor = interceptor;
            return this;
        }

        public AnnotationDelegateInterceptor build() {
            return new AnnotationDelegateInterceptor(annotation, interceptor);
        }
    }
}
