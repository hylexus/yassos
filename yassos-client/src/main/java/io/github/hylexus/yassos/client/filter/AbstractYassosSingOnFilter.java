package io.github.hylexus.yassos.client.filter;


import io.github.hylexus.yassos.client.redirect.DefaultRedirectStrategy;
import io.github.hylexus.yassos.client.redirect.RedirectStrategy;
import io.github.hylexus.yassos.client.session.HttpSessionInAccessor;
import io.github.hylexus.yassos.client.session.SessionInAccessor;
import io.github.hylexus.yassos.client.token.resolver.DefaultTokenResolver;
import io.github.hylexus.yassos.client.token.resolver.TokenResolver;
import io.github.hylexus.yassos.client.util.ServletUtils;
import io.github.hylexus.yassos.core.config.ConfigurationKeys;
import io.github.hylexus.yassos.core.session.YassosSession;
import io.github.hylexus.yassos.core.util.AntPathMatcher;
import io.github.hylexus.yassos.core.util.CommonUtils;
import io.github.hylexus.yassos.core.util.PathMatcher;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

import static io.github.hylexus.yassos.client.util.ServletUtils.initBooleanFromFilterConfig;
import static io.github.hylexus.yassos.client.util.ServletUtils.initStringFromFilterConfig;
import static io.github.hylexus.yassos.core.config.ConfigurationKeys.*;

/**
 * @author hylexus
 * Created At 2019-06-07 19:10
 */
@Slf4j
@Setter
@Getter
@Accessors(chain = true)
public abstract class AbstractYassosSingOnFilter implements Filter {

    protected PathMatcher pathMatcher = new AntPathMatcher();
    protected TokenResolver tokenResolver;

    protected RedirectStrategy redirectStrategy;

    protected SessionInAccessor sessionInAccessor;

    protected List<String> ignoreUriPatterns;

    protected String ssoServerLoginUrl;

    protected String ssoServerUrlPrefix;

    protected String clientSideLogoutUri;

    protected Boolean encodeUrl;

    protected Boolean throwExceptionIfTokenValidateException;

    protected Boolean useSession;

    protected String sessionKey;

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

        if (this.sessionInAccessor == null) {
            log.info("sessionInAccessor is null, use default implementation {} instead", HttpSessionInAccessor.class.getName());
            this.sessionInAccessor = new HttpSessionInAccessor();
        }
        if (this.ignoreUriPatterns == null) {
            this.ignoreUriPatterns = ServletUtils.initIgnoreUriPatternFromFilterConfig(filterConfig);
        }
        log.info("|:-- ignoredAntPatterns:");
        for (String pattern : this.ignoreUriPatterns) {
            log.info("\t{}", pattern);
        }
        // sso-server-url-prefix
        initStringFromFilterConfig(filterConfig, () -> this.ssoServerUrlPrefix == null, this::setSsoServerUrlPrefix, CONFIG_SOO_SERVER_URL_PREFIX, true);
        // sso-server login url
        initStringFromFilterConfig(filterConfig, () -> this.ssoServerLoginUrl == null, this::setSsoServerLoginUrl, CONFIG_SSO_SERVER_LOGIN_URL, true);
        // client logout url
        initStringFromFilterConfig(filterConfig, () -> this.clientSideLogoutUri == null, this::setClientSideLogoutUri, CONFIG_CLIENT_LOGOUT_URI, true);

        // throw-exception-if-validate-exception
        initBooleanFromFilterConfig(filterConfig, () -> this.throwExceptionIfTokenValidateException == null, this::setThrowExceptionIfTokenValidateException, CONFIG_THROW_EXCEPTION_IF_VALIDATE_EXCEPTION);
        // encode url
        initBooleanFromFilterConfig(filterConfig, () -> this.encodeUrl == null, this::setEncodeUrl, CONFIG_ENCODE_URL);

        // use session ?
        initBooleanFromFilterConfig(filterConfig, () -> this.useSession == null, this::setUseSession, CONFIG_USE_SESSION);
        initStringFromFilterConfig(filterConfig, () -> this.sessionKey == null, this::setSessionKey, CONFIG_SESSION_KEY, true);
        if (this.useSession && this.sessionKey == null) {
            throw new IllegalArgumentException(CONFIG_USE_SESSION.getName() + " was set to TRUE, but no " + CONFIG_SESSION_KEY.getName() + " specified");
        }

    }

    protected boolean shouldBeIgnored(String uri) {
        for (String pattern : this.ignoreUriPatterns) {
            if (this.pathMatcher.match(pattern, uri)) {
                log.debug("uri [{}] was ignored by pattern [{}]", uri, pattern);
                return true;
            }
        }
        return false;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        final HttpServletRequest req = (HttpServletRequest) request;
        final HttpServletResponse resp = (HttpServletResponse) response;

        try {
            final String requestURI = req.getRequestURI();
            log.debug(">>> requestURI = {}", requestURI);

            // 1. logout url ?
            if (this.isLogoutUrl(req)) {
                final String token = this.tokenResolver.resolveToken(req).orElse(null);

                this.preLogout(req, resp, token);
                if (StringUtils.isEmpty(token)) {
                    log.debug("tokenResolver returned a empty token, skipped");
                    this.postLogout(req, resp, token, null);
                    return;
                }

                boolean tokenDestroyedSuccessfully = false;
                try {
                    if (StringUtils.isNotEmpty(token)) {
                        tokenDestroyedSuccessfully = this.sessionInAccessor.destroyToken(token, generateTokenDestroyUrl(token));
                        log.debug("logout result = {}", tokenDestroyedSuccessfully);
                    }
                } finally {
                    this.postLogout(req, resp, token, tokenDestroyedSuccessfully);
                }
                log.debug("<<< logout.");
                return;
            }

            // 2. ignored ?
            if (this.shouldBeIgnored(requestURI)) {
                chain.doFilter(request, response);
                return;
            }

            // 3. resolve token from request
            final String token = this.tokenResolver.resolveToken(req).orElse(null);
            if (StringUtils.isEmpty(token)) {
                log.debug("redirect to login page (token is null or empty) ");
                this.redirectToLoginUrl(req, resp);
                return;
            }

            // 4. validate token from sso-server
            final YassosSession yassosSession = this.sessionInAccessor.fetchSessionInfo(token, generateTokenValidationUrl(token));
            if (yassosSession == null || !yassosSession.isValid()) {
                log.debug("redirect to login page (session invalid : {})", yassosSession);
                this.redirectToLoginUrl(req, resp);
                return;
            }

            if (this.useSession) {
                req.getSession().setAttribute(getSessionKey(), yassosSession);
            }
        } catch (Exception e) {
            if (this.throwExceptionIfTokenValidateException) {
                throw new ServletException(e);
            }

            log.error("redirect to login page (token validation fails)", e);
            this.redirectToLoginUrl(req, resp);
            return;
        }

        chain.doFilter(req, resp);
    }

    private boolean isLogoutUrl(HttpServletRequest req) {
        final String uri = req.getRequestURI();
        if (StringUtils.isEmpty(uri)) {
            return false;
        }

        return this.pathMatcher.match(this.clientSideLogoutUri, uri);
    }

    protected void preLogout(HttpServletRequest req, HttpServletResponse resp, String token) throws IOException {
    }

    protected void postLogout(HttpServletRequest req, HttpServletResponse resp, String token, Boolean tokenDestroyedSuccessfully) throws IOException {
    }

    protected void redirectToLoginUrl(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String targetUrl = generateRedirectToLoginUrl(req);
        this.redirectStrategy.redirect(req, resp, targetUrl);
    }

    protected final String getSessionKey() {
        return this.sessionKey == null ? CONFIG_SESSION_KEY.getDefaultValue() : this.sessionKey;
    }

    protected String generateRedirectToLoginUrl(HttpServletRequest request) {
        // eg. http://localhost:8080/yassos/login?cb=${originalUrl}
        if (this.encodeUrl) {
            return String.format("%s?%s=%s", ssoServerLoginUrl, ConfigurationKeys.CALLBACK_ADDRESS_NAME, this.encodeUrl(request.getRequestURL().toString()));
        }
        return String.format("%s?%s=%s", ssoServerLoginUrl, ConfigurationKeys.CALLBACK_ADDRESS_NAME, request.getRequestURL().toString());
    }

    protected String generateTokenDestroyUrl(String token) {
        final String signOutUri = ConfigurationKeys.CONFIG_TOKEN_DESTROY_URI.getDefaultValue();
        // eg. http://localhost:8080/sign-out?token=${token}
        return String.format("%s?token=%s",
                this.ssoServerUrlPrefix.endsWith("/") ?
                        this.ssoServerUrlPrefix + signOutUri :
                        this.ssoServerUrlPrefix + "/" + signOutUri,
                token
        );
    }

    protected String generateTokenValidationUrl(String token) {
        final String validateUriName = ConfigurationKeys.CONFIG_TOKEN_VALIDATION_URI.getDefaultValue();
        // eg. http://localhost:8080/validate?token=${token}
        return String.format("%s?token=%s",
                this.ssoServerUrlPrefix.endsWith("/") ?
                        this.ssoServerUrlPrefix + validateUriName :
                        this.ssoServerUrlPrefix + "/" + validateUriName,
                token
        );
    }

    protected String encodeUrl(String url) {
        return CommonUtils.encodeUrl(url);
    }

}
