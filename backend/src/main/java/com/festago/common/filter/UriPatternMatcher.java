package com.festago.common.filter;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.bind.annotation.RequestMethod;

public class UriPatternMatcher {

    private final AntPathMatcher antPathMatcher = new AntPathMatcher();
    private final List<Pattern> patterns = new ArrayList<>();

    public void addPattern(Set<RequestMethod> methods, Set<String> paths) {
        patterns.add(new Pattern(methods, paths));
    }

    public boolean match(RequestMethod method, String path) {
        for (Pattern pattern : patterns) {
            if (pattern.match(method, path)) {
                return true;
            }
        }
        return false;
    }

    private class Pattern {

        private final Set<RequestMethod> methods;
        private final Set<String> patterns;

        public Pattern(Set<RequestMethod> methods, Set<String> patterns) {
            this.methods = methods;
            this.patterns = patterns;
        }

        public boolean match(RequestMethod method, String path) {
            if (!methods.contains(method)) {
                return false;
            }
            for (String pattern : patterns) {
                if (antPathMatcher.match(pattern, path)) {
                    return true;
                }
            }
            return false;
        }
    }
}
