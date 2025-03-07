package io.github.hylexus.yassos.client.boot.config;

import io.github.hylexus.yassos.client.boot.props.YassosClientProps;
import io.github.hylexus.yassos.client.boot.props.YassosFilterMetadataProps;
import io.github.hylexus.yassos.client.boot.props.YassosServerProps;
import io.github.hylexus.yassos.client.filter.YassosSingOnFilter;
import io.github.hylexus.yassos.client.handler.BuiltinLogoutHandlerForDebugging;
import io.github.hylexus.yassos.client.handler.LogoutHandler;
import io.github.hylexus.yassos.client.redirect.DefaultRedirectStrategy;
import io.github.hylexus.yassos.client.redirect.RedirectStrategy;
import io.github.hylexus.yassos.client.session.HttpSessionInfoAccessor;
import io.github.hylexus.yassos.client.session.SessionInfoAccessor;
import io.github.hylexus.yassos.client.token.resolver.DefaultTokenResolver;
import io.github.hylexus.yassos.client.token.resolver.TokenResolver;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ansi.AnsiColor;
import org.springframework.boot.ansi.AnsiOutput;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static io.github.hylexus.yassos.client.boot.config.YassosClientConfigStatistics.BUILTIN_COMPONENT_COLOR;
import static io.github.hylexus.yassos.client.boot.config.YassosClientConfigStatistics.DEPRECATED_COMPONENT_COLOR;

/**
 * @author hylexus
 * Created At 2019-07-12 22:05
 */
@Slf4j
@Configuration
@EnableConfigurationProperties({
        YassosClientProps.class,
        YassosServerProps.class,
        YassosFilterMetadataProps.class
})
public class YassosClientAutoConfiguration {

    @Autowired
    private YassosClientProps clientProps;

    @Autowired
    private YassosServerProps serverProps;

    @Autowired
    private YassosFilterMetadataProps filterMetadataProps;

    @Bean
    @ConditionalOnProperty(prefix = "yassos.client", name = "enable", havingValue = "true")
    public FilterRegistrationBean<YassosSingOnFilter> yassosSingOnFilterFilterRegistrationBean(
            TokenResolver tokenResolver,
            SessionInfoAccessor sessionInfoAccessor,
            RedirectStrategy redirectStrategy,
            LogoutHandler logoutHandler) {

        final YassosSingOnFilter filter = new YassosSingOnFilter();

        filter.setTokenResolver(tokenResolver);
        filter.setSessionInfoAccessor(sessionInfoAccessor);
        filter.setRedirectStrategy(redirectStrategy);
        filter.setLogoutHandler(logoutHandler);

        filter.setIgnoreUriPatterns(clientProps.getIgnoreUriPatterns());
        filter.setClientSideLogoutUri(clientProps.getLogoutUri());
        filter.setEncodeUrl(clientProps.isEncodeUrl());
        filter.setThrowExceptionIfTokenValidateException(clientProps.isThrowExceptionIfTokenValidateException());
        filter.setUseSession(clientProps.isUseSession());
        filter.setSessionKey(clientProps.getSessionKey());

        filter.setSsoServerLoginUrl(serverProps.getSignOnUrl());
        filter.setSsoServerUrlPrefix(serverProps.getServerUrlPrefix());

        final FilterRegistrationBean<YassosSingOnFilter> registrationBean = new FilterRegistrationBean<>(filter);
        registrationBean.setUrlPatterns(filterMetadataProps.getUrlPatterns());
        registrationBean.setOrder(filterMetadataProps.getOrder());
        registrationBean.setName(filterMetadataProps.getName());
        return registrationBean;
    }

    @Bean
    @ConditionalOnMissingBean(LogoutHandler.class)
    public LogoutHandler logoutHandler() {
        log.warn(line(DEPRECATED_COMPONENT_COLOR,
                "|:-- Using BuiltinLogoutHandlerForDebugging, please consider to provide your own implementation of LogoutHandler"));
        return new BuiltinLogoutHandlerForDebugging();
    }

    @Bean
    @ConditionalOnMissingBean(TokenResolver.class)
    public TokenResolver tokenResolver() {
        log.info(line(BUILTIN_COMPONENT_COLOR, "|:-- Using DefaultTokenResolver"));
        return new DefaultTokenResolver();
    }

    @Bean
    @ConditionalOnMissingBean(RedirectStrategy.class)
    public RedirectStrategy redirectStrategy() {
        log.info(line(BUILTIN_COMPONENT_COLOR, "|:-- Using DefaultRedirectStrategy"));
        return new DefaultRedirectStrategy();
    }

    @Bean
    @ConditionalOnMissingBean(SessionInfoAccessor.class)
    public SessionInfoAccessor sessionInfoFetcher() {
        log.info(line(BUILTIN_COMPONENT_COLOR, "|:-- Using HttpSessionInfoAccessor"));
        return new HttpSessionInfoAccessor();
    }

    @Bean
    public YassosClientConfigStatistics yassosClientConfigStatistics() {
        return new YassosClientConfigStatistics();
    }

    private String line(AnsiColor color, String content) {
        return AnsiOutput.toString(color, content, AnsiColor.DEFAULT);
    }

}
