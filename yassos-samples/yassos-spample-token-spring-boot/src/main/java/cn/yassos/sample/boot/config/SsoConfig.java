package cn.yassos.sample.boot.config;

import io.github.hylexus.yassos.client.filter.AbstractYassosSingOnFilter;
import io.github.hylexus.yassos.client.filter.DefaultYassosSinOnFilter;
import io.github.hylexus.yassos.client.redirect.DefaultRedirectStrategy;
import io.github.hylexus.yassos.client.session.HttpSessionInfoFetcher;
import io.github.hylexus.yassos.client.token.resolver.DefaultTokenResolver;
import io.github.hylexus.yassos.core.config.ConfigurationKeys;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.HashSet;

/**
 * @author hylexus
 * Created At 2019-06-07 20:23
 */
@Configuration
public class SsoConfig {

    @Bean
    public FilterRegistrationBean<AbstractYassosSingOnFilter> filterRegistration() {
        final HashSet<String> ignoreAntPatterns = new HashSet<>();
        ignoreAntPatterns.add("/*.ico");
        ignoreAntPatterns.add("/*.css");
        ignoreAntPatterns.add("/*.js");

        final AbstractYassosSingOnFilter filter = new DefaultYassosSinOnFilter();
        filter.setRedirectStrategy(new DefaultRedirectStrategy());
        filter.setTokenResolver(new DefaultTokenResolver());
        filter.setSessionInfoFetcher(new HttpSessionInfoFetcher());
        filter.setIgnoreAntPatterns(ignoreAntPatterns);

        final FilterRegistrationBean<AbstractYassosSingOnFilter> registration = new FilterRegistrationBean<>(filter);

        registration.addInitParameter(ConfigurationKeys.CONFIG_SSO_SERVER_LOGIN_URL.getName(), "http://sso.mine.com:9000/login");
        registration.addInitParameter(ConfigurationKeys.CONFIG_SOO_SERVER_URL_PREFIX.getName(), "http://sso.mine.com:9000/");

        registration.setUrlPatterns(Arrays.asList("/*"));
//        registration.
        return registration;
    }
}
