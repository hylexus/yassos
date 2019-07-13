package io.github.hylexus.yassos.token;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;

/**
 * @author hylexus
 * Created At 2019-06-16 17:17
 */
@Slf4j
public class PathTest {
    @Test
    public void test2() {
        PathMatcher matcher = new AntPathMatcher();

        System.out.println(matcher.match("/api", "/"));
        System.out.println(matcher.match("/api", "/api/user"));
        System.out.println(matcher.match("/api/*", "/api/user"));
        System.out.println(matcher.match("/api/*", "/api/user/list"));
        System.out.println(matcher.match("/api/**", "/api/user/list/all"));
    }
}
