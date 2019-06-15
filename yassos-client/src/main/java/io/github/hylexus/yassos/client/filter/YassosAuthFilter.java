package io.github.hylexus.yassos.client.filter;


import io.github.hylexus.yassos.client.config.ConfigurationKey;
import io.github.hylexus.yassos.client.exception.TokenValidateException;
import io.github.hylexus.yassos.client.model.SessionInfo;
import io.github.hylexus.yassos.client.service.RedirectStrategy;
import io.github.hylexus.yassos.client.service.SessionInfoFetcher;
import io.github.hylexus.yassos.client.service.TokenResolver;
import io.github.hylexus.yassos.client.service.impl.DefaultRedirectStrategy;
import io.github.hylexus.yassos.client.service.impl.DefaultTokenResolver;
import io.github.hylexus.yassos.client.utils.CommonUtils;
import io.github.hylexus.yassos.client.utils.ConfigurationKeys;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;

import static io.github.hylexus.yassos.client.utils.ConfigurationKeys.*;

/**
 * @author hylexus
 * Created At 2019-06-07 19:10
 */
@Slf4j
@Setter
@Getter
@Accessors(chain = true)
public class YassosAuthFilter implements Filter {

    private TokenResolver tokenResolver;

    private RedirectStrategy redirectStrategy;

    private SessionInfoFetcher sessionInfoFetcher;

    private String loginUrl;

    private Boolean encodeUrl;

    private String serverUrlPrefix;

    private Boolean throwExceptionIfTokenValidateException;

    private Boolean useSession;

    private String sessionKey;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

        if (this.redirectStrategy == null) {
            log.info("redirectStrategy is null, use default implementation {} instead", DefaultRedirectStrategy.class.getName());
            this.redirectStrategy = new DefaultRedirectStrategy();
        }

        if (this.tokenResolver == null) {
            log.info("tokenResolver is null, use default implementation {} instead", DefaultTokenResolver.class.getName());
            this.tokenResolver = new DefaultTokenResolver();
        }
        // server-url-prefix
        initString(filterConfig, () -> this.serverUrlPrefix == null, this::setServerUrlPrefix, CONFIG_SOO_SERVER_URL_PREFIX, true);
        // SSO server login url
        initString(filterConfig, () -> this.loginUrl == null, this::setLoginUrl, CONFIG_SSO_SERVER_LOGIN_URL, true);

        // throw-exception-if-validate-exception
        initBoolean(filterConfig, () -> this.throwExceptionIfTokenValidateException == null, this::setThrowExceptionIfTokenValidateException, CONFIG_THROW_EXCEPTION_IF_VALIDATE_EXCEPTION);
        // encode url
        initBoolean(filterConfig, () -> this.encodeUrl == null, this::setEncodeUrl, CONFIG_ENCODE_URL);

        // use session ?
        initBoolean(filterConfig, () -> this.useSession == null, this::setUseSession, CONFIG_USE_SESSION);
        initString(filterConfig, () -> this.sessionKey == null, this::setSessionKey, CONFIG_SESSION_KEY, true);
        if (this.useSession && this.sessionKey == null) {
            throw new IllegalArgumentException(CONFIG_USE_SESSION.getName() + " was set to TRUE, but no " + CONFIG_SESSION_KEY.getName() + " specified");
        }

    }

    private void initString(FilterConfig filterConfig, BooleanSupplier supplier, Consumer<String> consumer, ConfigurationKey<String> configurationKey, boolean forceValidate) {
        if (!supplier.getAsBoolean())
            return;
        final String name = configurationKey.getName();
        final String defaultValue = configurationKey.getDefaultValue();
        final String initParameter = filterConfig.getInitParameter(name);
        if (StringUtils.isEmpty(initParameter)) {
            log.info("[{}] is null, use default value <{}>", name, defaultValue);
            consumer.accept(defaultValue);
        } else {
            log.info("parse [{}] from filterConfig, value : <{}>", name, initParameter);
            configurationKey.validate(initParameter, forceValidate);
            consumer.accept(initParameter);
        }

    }

    private void initBoolean(FilterConfig filterConfig, BooleanSupplier supplier, Consumer<Boolean> consumer, ConfigurationKey<Boolean> configKey) {
        if (!supplier.getAsBoolean())
            return;

        final String name = configKey.getName();
        final Boolean defaultValue = configKey.getDefaultValue();
        final String initParameter = filterConfig.getInitParameter(name);
        if (StringUtils.isEmpty(initParameter)) {
            log.info("[{}] is null, use default value <{}>", name, defaultValue);
            consumer.accept(defaultValue);
        } else {
            try {
                consumer.accept(Boolean.parseBoolean(initParameter));
            } catch (Exception e) {
                consumer.accept(defaultValue);
                log.error("parse [{}] error, use default value <{}>", name, defaultValue);
            }
        }

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        final HttpServletRequest req = (HttpServletRequest) request;
        final HttpServletResponse resp = (HttpServletResponse) response;

        try {
            // 1. resolve token from request
            final String token = this.tokenResolver.resolveToken(req).orElse(null);
            if (StringUtils.isEmpty(token)) {
                this.redirectToLoginUrl(req, resp);
                return;
            }

            // 2. validate token from sso-server
            final SessionInfo sessionInfo = this.sessionInfoFetcher.fetchSessionInfo(token, generateTokenValidationUrl(token));
            if (sessionInfo == null || !sessionInfo.isValid()) {
                this.redirectToLoginUrl(req, resp);
                return;
            }

            if (this.useSession) {
                req.getSession().setAttribute(getSessionKey(), sessionInfo);
            }
        } catch (TokenValidateException e) {
            if (this.throwExceptionIfTokenValidateException) {
                throw new ServletException(e);
            }

            this.redirectToLoginUrl(req, resp);
            log.error("token validation fails.", e);
            return;
        }

        chain.doFilter(req, resp);
    }

    private void redirectToLoginUrl(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String targetUrl = generateRedirectToLoginUrl(req);
        this.redirectStrategy.redirect(req, resp, targetUrl);
    }

    protected final String getSessionKey() {
        return this.sessionKey == null ? CONFIG_SESSION_KEY.getDefaultValue() : this.sessionKey;
    }

    private String generateRedirectToLoginUrl(HttpServletRequest request) {
        // eg. http://localhost:8080/yassos/login?cb=${originalUrl}
        if (this.encodeUrl) {
            return String.format("%s?%s=%s", loginUrl, ConfigurationKeys.CALLBACK_ADDRESS_NAME, this.encodeUrl(request.getRequestURL().toString()));
        }
        return String.format("%s?%s=%s", loginUrl, ConfigurationKeys.CALLBACK_ADDRESS_NAME, request.getRequestURL().toString());
    }

    private String generateTokenValidationUrl(String token) {
        final String validateUriName = ConfigurationKeys.CONFIG_TOKEN_VALIDATION_URI.getDefaultValue();
        // eg. http://localhost:8080/validate?token=${token}
        return String.format("%s?token=%s",
                this.serverUrlPrefix.endsWith("/") ?
                        this.serverUrlPrefix + validateUriName :
                        this.serverUrlPrefix + "/" + validateUriName,
                token
        );
    }

    protected String encodeUrl(String url) {
        return CommonUtils.encodeUrl(url);
    }

}
