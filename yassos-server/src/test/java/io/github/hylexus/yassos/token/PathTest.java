package io.github.hylexus.yassos.token;

import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertFalse;

/**
 * @author hylexus
 * Created At 2019-06-16 17:17
 */
@Slf4j
public class PathTest {
    @Test
    public void antPathMatcherTest() {
        PathMatcher matcher = new AntPathMatcher();

        assertFalse(matcher.match("/api", "/"));
        assertFalse(matcher.match("/api", "/api/user"));
        assertTrue(matcher.match("/api/*", "/api/user"));
        assertFalse(matcher.match("/api/*", "/api/user/list"));
        assertTrue(matcher.match("/api/**", "/api/user/list/all"));
    }
}
