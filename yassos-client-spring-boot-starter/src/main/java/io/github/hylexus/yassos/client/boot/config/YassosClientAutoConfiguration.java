package io.github.hylexus.yassos.client.boot.config;

import io.github.hylexus.yassos.client.boot.props.YassosClientProps;
import io.github.hylexus.yassos.client.boot.props.YassosFilterMetadataProps;
import io.github.hylexus.yassos.client.boot.props.YassosServerProps;
import io.github.hylexus.yassos.client.filter.AbstractYassosSingOnFilter;
import io.github.hylexus.yassos.client.filter.DefaultYassosSinOnFilter;
import io.github.hylexus.yassos.client.redirect.DefaultRedirectStrategy;
import io.github.hylexus.yassos.client.redirect.RedirectStrategy;
import io.github.hylexus.yassos.client.session.HttpSessionInAccessor;
import io.github.hylexus.yassos.client.session.SessionInAccessor;
import io.github.hylexus.yassos.client.token.resolver.DefaultTokenResolver;
import io.github.hylexus.yassos.client.token.resolver.TokenResolver;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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
    public FilterRegistrationBean<AbstractYassosSingOnFilter> yassosSingOnFilterFilterRegistrationBean(
            TokenResolver tokenResolver,
            SessionInAccessor sessionInAccessor,
            RedirectStrategy redirectStrategy) {

        final AbstractYassosSingOnFilter filter = new DefaultYassosSinOnFilter();

        filter.setTokenResolver(tokenResolver);
        filter.setSessionInAccessor(sessionInAccessor);
        filter.setRedirectStrategy(redirectStrategy);

        filter.setIgnoreUriPatterns(clientProps.getIgnoreUriPatterns());
        filter.setClientSideLogoutUri(clientProps.getLogoutUri());
        filter.setEncodeUrl(clientProps.isEncodeUrl());
        filter.setThrowExceptionIfTokenValidateException(clientProps.isThrowExceptionIfTokenValidateException());
        filter.setUseSession(clientProps.isUseSession());
        filter.setSessionKey(clientProps.getSessionKey());

        filter.setSsoServerLoginUrl(serverProps.getSignOnUrl());
        filter.setSsoServerLogoutUrl(serverProps.getSignOutUrl());
        filter.setSsoServerUrlPrefix(serverProps.getServerUrlPrefix());

        final FilterRegistrationBean<AbstractYassosSingOnFilter> registrationBean = new FilterRegistrationBean<>(filter);
        registrationBean.setUrlPatterns(filterMetadataProps.getUrlPatterns());
        registrationBean.setOrder(filterMetadataProps.getOrder());
        registrationBean.setName(filterMetadataProps.getName());
        return registrationBean;
    }

    @Bean
    @ConditionalOnMissingBean(TokenResolver.class)
    public TokenResolver tokenResolver() {
        log.info("|:-- use DefaultTokenResolver");
        return new DefaultTokenResolver();
    }

    @Bean
    @ConditionalOnMissingBean(RedirectStrategy.class)
    public RedirectStrategy redirectStrategy() {
        log.info("|:-- use DefaultRedirectStrategy");
        return new DefaultRedirectStrategy();
    }

    @Bean
    @ConditionalOnMissingBean(SessionInAccessor.class)
    public SessionInAccessor sessionInfoFetcher() {
        log.info("|:-- use HttpSessionInAccessor");
        return new HttpSessionInAccessor();
    }
}
