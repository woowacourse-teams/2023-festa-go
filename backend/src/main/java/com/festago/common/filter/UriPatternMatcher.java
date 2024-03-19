package com.festago.common.filter;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import org.springframework.util.AntPathMatcher;

public class UriPatternMatcher {

    private final AntPathMatcher antPathMatcher = new AntPathMatcher();
    private final List<Pattern> patterns = new ArrayList<>();

    public void addPattern(Set<String> methods, Set<String> paths) {
        patterns.add(new Pattern(methods, paths));
    }

    public boolean match(String method, String path) {
        for (Pattern pattern : patterns) {
            if (pattern.match(method, path)) {
                return true;
            }
        }
        return false;
    }

    private class Pattern {

        private final Set<String> methods;
        private final Set<String> patterns;

        public Pattern(Set<String> methods, Set<String> patterns) {
            this.methods = methods;
            this.patterns = patterns;
        }

        public boolean match(String method, String path) {
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
