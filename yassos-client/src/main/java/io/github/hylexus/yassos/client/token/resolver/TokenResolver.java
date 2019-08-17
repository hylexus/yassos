package io.github.hylexus.yassos.client.token.resolver;

import io.github.hylexus.yassos.client.exception.TokenValidateException;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

/**
 * @author hylexus
 * Created At 2019-06-07 19:22
 */
@FunctionalInterface
public interface TokenResolver {

    /**
     * Resolve token from HttpServletRequest
     *
     * @param request HttpServletRequest
     * @return a token parsed from {@code request}
     * @throws TokenValidateException TokenValidateException
     */
    Optional<String> resolveToken(HttpServletRequest request) throws TokenValidateException;

}
