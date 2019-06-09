package io.github.hylexus.yassos.client.filter;


import io.github.hylexus.yassos.client.config.RedirectStrategy;
import io.github.hylexus.yassos.client.config.TokenResolver;
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

import static io.github.hylexus.yassos.client.utils.ConfigurationKeys.CONFIG_SSO_LOGIN_URL;

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

        if (this.redirectStrategy == null) {
            log.info("redirectStrategy is null, use default implementation {} instead", RedirectStrategy.DefaultRedirectStrategy.class.getName());
            this.redirectStrategy = new RedirectStrategy.DefaultRedirectStrategy();
        }

        if (this.tokenResolver == null) {
            log.info("tokenResolver is null, use default implementation {} instead", TokenResolver.DefaultTokenResolver.class.getName());
            this.tokenResolver = new TokenResolver.DefaultTokenResolver();
        }
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        final HttpServletRequest req = (HttpServletRequest) request;
        final HttpServletResponse resp = (HttpServletResponse) response;

        String token = this.tokenResolver.resolveToken(req).orElse(null);
        if (StringUtils.isEmpty(token)) {
            String targetUrl = CommonUtils.generateRedirectToLoginUrl(this.ssoLoginUrl, req.getRequestURL().toString());
            this.redirectStrategy.redirect(req, resp, targetUrl);
            return;
        }

        chain.doFilter(req, resp);
    }
}
