package cn.yassos.sample.boot.config;

import io.github.hylexus.yassos.client.filter.YassosAuthFilter;
import io.github.hylexus.yassos.client.service.impl.BearerTokenResolver;
import io.github.hylexus.yassos.client.service.impl.DefaultRedirectStrategy;
import io.github.hylexus.yassos.client.service.impl.HttpSessionInfoFetcher;
import io.github.hylexus.yassos.client.utils.ConfigurationKeys;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

/**
 * @author hylexus
 * Created At 2019-06-07 20:23
 */
@Configuration
public class SsoConfig {

    @Bean
    public FilterRegistrationBean<YassosAuthFilter> filterRegistration() {
        final YassosAuthFilter filter = new YassosAuthFilter();
        filter.setRedirectStrategy(new DefaultRedirectStrategy());
        filter.setTokenResolver(new BearerTokenResolver());
//        filter.setTokenResolver(new DefaultTokenResolver());
        filter.setSessionInfoFetcher(new HttpSessionInfoFetcher());

        final FilterRegistrationBean<YassosAuthFilter> registration = new FilterRegistrationBean<>();

        registration.setFilter(filter);
        registration.addInitParameter(ConfigurationKeys.CONFIG_SSO_SERVER_LOGIN_URL.getName(), "http://sso.mine.com:9000/login");
        registration.addInitParameter(ConfigurationKeys.CONFIG_SOO_SERVER_URL_PREFIX.getName(), "http://sso.mine.com:9000/");

        registration.setUrlPatterns(Arrays.asList("/*"));
        return registration;
    }
}
