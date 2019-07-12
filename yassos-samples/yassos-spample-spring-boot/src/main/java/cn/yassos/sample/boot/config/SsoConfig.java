package cn.yassos.sample.boot.config;

import io.github.hylexus.yassos.client.token.resolver.DefaultTokenResolver;
import io.github.hylexus.yassos.client.token.resolver.TokenResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author hylexus
 * Created At 2019-06-07 20:23
 */
@Configuration
public class SsoConfig {

    @Bean
    public TokenResolver tokenResolver() {
        return new DefaultTokenResolver();
    }
}
