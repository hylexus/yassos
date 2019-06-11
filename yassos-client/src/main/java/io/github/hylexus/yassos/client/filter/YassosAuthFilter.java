package io.github.hylexus.yassos.client.filter;


import io.github.hylexus.yassos.client.config.ConfigurationKey;
import io.github.hylexus.yassos.client.exception.TokenValidateException;
import io.github.hylexus.yassos.client.model.SessionInfo;
import io.github.hylexus.yassos.client.service.RedirectStrategy;
import io.github.hylexus.yassos.client.service.TokenResolver;
import io.github.hylexus.yassos.client.service.TokenValidator;
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

    private TokenValidator tokenValidator;

    private String loginUrl;

    private Boolean encodeUrl;

    private String serverUrlPrefix;

    private Boolean throwExceptionIfTokenValidateException;

    private Boolean useSession;

    private String sessionKey;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

        if (this.redirectStrategy == null) {
            log.info("redirectStrategy is null, use default implementation {} instead", RedirectStrategy.DefaultRedirectStrategy.class.getName());
            this.redirectStrategy = new RedirectStrategy.DefaultRedirectStrategy();
        }

        if (this.tokenResolver == null) {
            log.info("tokenResolver is null, use default implementation {} instead", TokenResolver.DefaultTokenResolver.class.getName());
            this.tokenResolver = new TokenResolver.DefaultTokenResolver();
        }
        // server-url-prefix
        initString(filterConfig, () -> this.serverUrlPrefix == null, this::setServerUrlPrefix, CONFIG_SERVER_URL_PREFIX, true);
        // SSO server login url
        initString(filterConfig, () -> this.loginUrl == null, this::setLoginUrl, CONFIG_SSO_LOGIN_URL, true);

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
                String targetUrl = generateRedirectToLoginUrl(req.getRequestURL().toString());
                this.redirectStrategy.redirect(req, resp, targetUrl);
                return;
            }

            // 2. validate token from sso-server
            final SessionInfo sessionInfo = this.tokenValidator.validateToken(token, generateTokenValidationUrl(token));
            if (sessionInfo == null || !sessionInfo.isValid()) {
                String targetUrl = CommonUtils.generateRedirectToLoginUrl(this.loginUrl, req.getRequestURL().toString(), this.encodeUrl);
                this.redirectStrategy.redirect(req, resp, targetUrl);
                return;
            }

            if (this.useSession) {
                req.getSession().setAttribute(getSessionKey(), sessionInfo);
            }
        } catch (TokenValidateException e) {
            if (this.throwExceptionIfTokenValidateException) {
                throw new ServletException(e);
            }
        }

        chain.doFilter(req, resp);
    }

    protected final String getSessionKey() {
        return this.sessionKey == null ? CONFIG_SESSION_KEY.getDefaultValue() : this.sessionKey;
    }

    private String generateRedirectToLoginUrl(String originalUrl) {
        // eg. http://localhost:8080/yassos/login?cb=${originalUrl}
        if (this.encodeUrl) {
            return String.format("%s?%s=%s", loginUrl, ConfigurationKeys.CALLBACK_ADDRESS_NAME, this.encodeUrl(originalUrl));
        }
        return String.format("%s?%s=%s", loginUrl, ConfigurationKeys.CALLBACK_ADDRESS_NAME, originalUrl);
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

    private String encodeUrl(String url) {
        return url;
    }

}
