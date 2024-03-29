package com.festago.common.filter.wrapping;

import java.util.Collections;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.bind.annotation.RequestMethod;

@Component
public class UriPatternMatcher {

    private final AntPathMatcher antPathMatcher = new AntPathMatcher();
    private final Map<RequestMethod, Set<String>> methodToPatterns = new EnumMap<>(RequestMethod.class);

    /**
     * HttpMethod 목록에 대해 Pattern에 추가될 URI Path 목록을 추가하는 메서드<br/> 어플리케이션이 실행될 때, 하나의 스레드에서 접근하는 것을 가정으로 설계했기에 Thread Safe
     * 하지 않음.<br/> 따라서 다른 Bean에서 해당 클래스를 의존하여, 이 메서드를 호출하는 것에 주의할 것
     *
     * @param methods 패턴에 추가할 HttpMethod 목록
     * @param paths   패턴에 추가할 URI 목록
     */
    public void addPattern(Set<RequestMethod> methods, Set<String> paths) {
        for (RequestMethod method : methods) {
            Set<String> patterns = methodToPatterns.computeIfAbsent(method, ignore -> new HashSet<>());
            patterns.addAll(paths);
        }
    }

    /**
     * HttpMethod와 Path이 등록된 패턴에 일치하는지 검사하는 메서드
     *
     * @param method 패턴에 일치하는지 검사할 HttpMethod
     * @param path   패턴에 일치하는지 검사할 경로. 예시: "/api/v1/festival"
     * @return method에 대한 path가 등록된 패턴에 일치하면 true, 아니면 false
     */
    public boolean match(RequestMethod method, String path) {
        Set<String> patterns = methodToPatterns.getOrDefault(method, Collections.emptySet());
        for (String pattern : patterns) {
            if (antPathMatcher.match(pattern, path)) {
                return true;
            }
        }
        return false;
    }
}
