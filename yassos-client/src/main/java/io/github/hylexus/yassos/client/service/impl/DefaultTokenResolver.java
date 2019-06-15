package io.github.hylexus.yassos.client.service.impl;

import io.github.hylexus.yassos.client.exception.TokenValidateException;
import io.github.hylexus.yassos.client.service.TokenResolver;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

import static io.github.hylexus.yassos.client.utils.ConfigurationKeys.CONFIG_TOKEN;

public class DefaultTokenResolver implements TokenResolver {

    @Override
    public Optional<String> resolveToken(HttpServletRequest request) throws TokenValidateException {

        final String tokenKey = CONFIG_TOKEN.getDefaultValue();
        final String header = request.getHeader(tokenKey);
        if (StringUtils.isNotBlank(header))
            return Optional.of(header);

        final String requestParam = request.getParameter(tokenKey);
        if (StringUtils.isNotEmpty(requestParam))
            return Optional.of(requestParam);

        final Cookie[] cookies = request.getCookies();
        if (cookies == null || cookies.length <= 0)
            return Optional.empty();

        for (Cookie cookie : cookies) {
            if (cookie.getName().equalsIgnoreCase(tokenKey)) {
                return Optional.ofNullable(cookie.getValue());
            }
        }

        return Optional.empty();
    }
}