package io.github.hylexus.yassos.client.filter;


import io.github.hylexus.yassos.client.config.ConfigurationKey;
import io.github.hylexus.yassos.client.config.RedirectStrategy;
import io.github.hylexus.yassos.client.config.TokenResolver;
import io.github.hylexus.yassos.client.config.TokenValidator;
import io.github.hylexus.yassos.client.model.SessionInfo;
import io.github.hylexus.yassos.client.utils.CommonUtils;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

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
    private String ssoLoginUrl;

    private TokenResolver tokenResolver;

    private RedirectStrategy redirectStrategy;

    private TokenValidator tokenValidator;

    private Boolean encodeUrl;

    private String serverUrlPrefix;


    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        if (this.ssoLoginUrl == null) {
            String loginUrl = filterConfig.getInitParameter(CONFIG_SSO_LOGIN_URL.getName());
            log.info("parse ssoLoginUrl from filterConfig, value : {}", loginUrl);
            if (StringUtils.isEmpty(loginUrl)) {
                throw new IllegalArgumentException(CONFIG_SSO_LOGIN_URL.getName() + " must be not empty");
            }
            this.ssoLoginUrl = loginUrl;
        }

        if (this.serverUrlPrefix == null) {
            final ConfigurationKey<String> key = CONFIG_SERVER_URL_PREFIX;
            final String initParameter = filterConfig.getInitParameter(key.getName());
            log.info("parse ssoLoginUrl from filterConfig, value : {}", initParameter);
            key.validate(initParameter, "ssoLoginUrl is invalid");
        }

        if (this.redirectStrategy == null) {
            log.info("redirectStrategy is null, use default implementation {} instead", RedirectStrategy.DefaultRedirectStrategy.class.getName());
            this.redirectStrategy = new RedirectStrategy.DefaultRedirectStrategy();
        }

        if (this.tokenResolver == null) {
            log.info("tokenResolver is null, use default implementation {} instead", TokenResolver.DefaultTokenResolver.class.getName());
            this.tokenResolver = new TokenResolver.DefaultTokenResolver();
        }

        if (encodeUrl == null) {
            String encodeUrl = filterConfig.getInitParameter(CONFIG_ENCODE_URL.getName());
            if (encodeUrl == null) {
                this.encodeUrl = CONFIG_ENCODE_URL.getDefaultValue();
                log.info("encodeUrl url is null, use default value {}", CONFIG_ENCODE_URL.getDefaultValue());
            } else {
                try {
                    this.encodeUrl = Boolean.parseBoolean(encodeUrl);
                } catch (Exception e) {
                    log.error("parse encodeUrl error, use default value {}", CONFIG_ENCODE_URL.getDefaultValue());
                }
            }
        }

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        final HttpServletRequest req = (HttpServletRequest) request;
        final HttpServletResponse resp = (HttpServletResponse) response;

        final String token = this.tokenResolver.resolveToken(req).orElse(null);
        if (StringUtils.isEmpty(token)) {
            String targetUrl = generateRedirectToLoginUrl(req.getRequestURL().toString());
            this.redirectStrategy.redirect(req, resp, targetUrl);
            return;
        }

        final SessionInfo sessionInfo = this.tokenValidator.validateToken(token, generateTokenValidationUrl(token));
        if (sessionInfo == null || !sessionInfo.isValid()) {
            String targetUrl = CommonUtils.generateRedirectToLoginUrl(this.ssoLoginUrl, req.getRequestURL().toString(), this.encodeUrl);
            this.redirectStrategy.redirect(req, resp, targetUrl);
        }
        chain.doFilter(req, resp);
    }

    private String generateRedirectToLoginUrl(String originalUrl) {
        // eg. http://localhost:8080/yassos/login?cb=${originalUrl}
        if (this.encodeUrl) {
            return String.format("%s?%s=%s", ssoLoginUrl, CALLBACK_ADDRESS_NAME, this.encodeUrl(originalUrl));
        }
        return String.format("%s?%s=%s", ssoLoginUrl, CALLBACK_ADDRESS_NAME, originalUrl);
    }

    private String generateTokenValidationUrl(String token) {
        final String validateUriName = CONFIG_TOKEN_VALIDATION_URI.getDefaultValue();
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
