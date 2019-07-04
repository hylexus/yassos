package io.github.hylexus.yassos;

import io.github.hylexus.yassos.service.TokenGenerator;
import io.github.hylexus.yassos.service.UserDetailService;
import io.github.hylexus.yassos.support.auth.CredentialsMatcher;
import io.github.hylexus.yassos.support.model.DefaultUserDetails;
import io.github.hylexus.yassos.support.model.UserDetails;
import io.github.hylexus.yassos.support.session.BuiltinRedisSessionManager;
import io.github.hylexus.yassos.support.session.SessionInfoEnhancer;
import io.github.hylexus.yassos.support.session.SessionManager;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

import java.util.Random;

/**
 * @author hylexus
 * Created At 2019-06-07 16:38
 */
@SpringBootApplication
public class YassosServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(YassosServerApplication.class, args);
    }

    @Bean
    public TokenGenerator tokenGenerator() {
        return new TokenGenerator.DefaultTokenGenerator();
    }

    @Bean
    public SessionManager sessionManager() {
        return new BuiltinRedisSessionManager();
    }

    @Bean
    public UserDetailService userDetailService() {
        return new UserDetailService() {
            @Override
            public UserDetails loadByUsername(String username) {
                return new DefaultUserDetails()
                        .setUsername(username)
                        .setPassword("123456")
                        .setUserId(new Random().nextLong())
                        .setLocked(false)
                        .setCredentialExpired(false);
            }
        };
    }

    @Bean
    @ConditionalOnMissingBean(CredentialsMatcher.class)
    public CredentialsMatcher credentialsMatcher() {
        return new CredentialsMatcher.PlainTextCredentialsMatcher();
    }

    @Bean
    @ConditionalOnMissingBean(SessionInfoEnhancer.class)
    public SessionInfoEnhancer sessionInfoEnhancer() {
        return new SessionInfoEnhancer.NoneEnhancementEnhancer();
    }
}
