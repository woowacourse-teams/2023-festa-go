package com.festago.common.interceptor;

import static java.util.stream.Collectors.toUnmodifiableSet;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.HandlerInterceptor;

public class HttpMethodDelegateInterceptor implements HandlerInterceptor {

    private final Set<String> allowMethods;
    private final HandlerInterceptor interceptor;

    protected HttpMethodDelegateInterceptor(Set<String> allowMethods, HandlerInterceptor interceptor) {
        this.allowMethods = allowMethods;
        this.interceptor = interceptor;
    }

    public static HttpMethodDelegateInterceptorBuilder builder() {
        return new HttpMethodDelegateInterceptorBuilder();
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
        throws Exception {
        if (allowMethods.contains(request.getMethod())) {
            return interceptor.preHandle(request, response, handler);
        }
        return true;
    }

    public static class HttpMethodDelegateInterceptorBuilder {

        private final Set<HttpMethod> allowMethod = new HashSet<>();
        private HandlerInterceptor interceptor;

        public HttpMethodDelegateInterceptorBuilder allowMethod(HttpMethod... httpMethods) {
            allowMethod.addAll(Arrays.asList(httpMethods));
            return this;
        }

        public HttpMethodDelegateInterceptorBuilder interceptor(HandlerInterceptor interceptor) {
            this.interceptor = interceptor;
            return this;
        }

        public HttpMethodDelegateInterceptor build() {
            Set<String> methods = allowMethod.stream()
                .map(HttpMethod::name)
                .collect(toUnmodifiableSet());
            return new HttpMethodDelegateInterceptor(methods, interceptor);
        }
    }
}
