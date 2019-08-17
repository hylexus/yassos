package io.github.hylexus.yassos.client.token.resolver;

import io.github.hylexus.yassos.client.exception.TokenValidateException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

import static io.github.hylexus.yassos.core.config.ConfigurationKeys.CONFIG_TOKEN;

/**
 * @author hylexus
 */
@Slf4j
public class DefaultTokenResolver implements TokenResolver {

    @Override
    public Optional<String> resolveToken(HttpServletRequest request) throws TokenValidateException {

        final String tokenKey = CONFIG_TOKEN.getDefaultValue();
        log.debug("1. Attempt to parse [{}] from RequestHeader", tokenKey);
        final String header = request.getHeader(tokenKey);
        if (StringUtils.isNotBlank(header)) {
            log.debug("\t{} = {}", tokenKey, header);
            return Optional.of(header);
        }

        log.debug("\t{} = {} (IGNORED)", tokenKey, header);
        log.debug("2. Attempt to parse [{}] from RequestParams", tokenKey);
        final String requestParam = request.getParameter(tokenKey);
        if (StringUtils.isNotEmpty(requestParam)) {
            log.debug("\t{} = {}", tokenKey, requestParam);
            return Optional.of(requestParam);
        }

        log.debug("\t{} = {} (IGNORED)", tokenKey, requestParam);
        log.debug("3. Attempt to parse [{}] from Cookies", tokenKey);
        final Cookie[] cookies = request.getCookies();
        if (cookies == null || cookies.length <= 0) {
            log.debug("\tno Cookies enabled.");
            return Optional.empty();
        }

        for (Cookie cookie : cookies) {
            if (cookie.getName().equalsIgnoreCase(tokenKey)) {
                log.debug("\t{} = {}", tokenKey, cookie.getValue());
                return Optional.ofNullable(cookie.getValue());
            }
        }
        log.debug("\t{} = null", tokenKey);

        return Optional.empty();
    }
}