package cn.yassos.sample.boot.config;

import io.github.hylexus.yassos.client.filter.YassosAuthFilter;
import io.github.hylexus.yassos.client.service.HttpTokenValidator;
import io.github.hylexus.yassos.client.service.RedirectStrategy;
import io.github.hylexus.yassos.client.service.TokenResolver;
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
        filter.setRedirectStrategy(new RedirectStrategy.DefaultRedirectStrategy());
        filter.setTokenResolver(new TokenResolver.DefaultTokenResolver());
        filter.setTokenValidator(new HttpTokenValidator());

        final FilterRegistrationBean<YassosAuthFilter> registration = new FilterRegistrationBean<>();

        registration.setFilter(filter);
        registration.addInitParameter(ConfigurationKeys.CONFIG_SSO_LOGIN_URL.getName(), "http://localhost:9000/login");
        registration.addInitParameter(ConfigurationKeys.CONFIG_SERVER_URL_PREFIX.getName(), "http://localhost:9000/");

        registration.setUrlPatterns(Arrays.asList("/*"));
        return registration;
    }
}
