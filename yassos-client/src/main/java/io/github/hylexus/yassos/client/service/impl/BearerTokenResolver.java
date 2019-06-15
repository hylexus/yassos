package io.github.hylexus.yassos.client.service.impl;

import io.github.hylexus.yassos.client.exception.TokenValidateException;
import io.github.hylexus.yassos.client.service.TokenResolver;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

/**
 * @author hylexus
 * Created At 2019-06-15 18:06
 */
@Slf4j
public class BearerTokenResolver implements TokenResolver {
    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String AUTHORIZATION_TOKEN_PREFIX = "Bearer ";
    public static final int AUTHORIZATION_TOKEN_PREFIX_LENGTH = AUTHORIZATION_TOKEN_PREFIX.length();

    public static final String AUTHORIZATION_TOKEN = "access_token";

    @Override
    public Optional<String> resolveToken(HttpServletRequest request) throws TokenValidateException {

        log.debug("1. Attempt to parse [{}] from RequestHeader", AUTHORIZATION_HEADER);
        final String bearer = request.getHeader(AUTHORIZATION_HEADER);
        if (StringUtils.isNotEmpty(bearer) && bearer.startsWith(AUTHORIZATION_TOKEN_PREFIX)) {
            log.debug("\ttokenValue : {}", bearer);
            if (bearer.length() > AUTHORIZATION_TOKEN_PREFIX_LENGTH) {
                return Optional.of(bearer.substring(AUTHORIZATION_TOKEN_PREFIX_LENGTH));
            }
        }
        log.warn("\ttokenValue : {}, IGNORED(not a valid BearerToken format).", bearer);

        log.debug("2. Attempt to parse [{}] from request parameters.", AUTHORIZATION_TOKEN);
        final String parameter = request.getParameter(AUTHORIZATION_TOKEN);
        log.debug("\ttokenValue : {}", parameter);

        return Optional.ofNullable(parameter);
    }
}
