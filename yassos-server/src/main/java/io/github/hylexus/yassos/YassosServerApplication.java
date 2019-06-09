package io.github.hylexus.yassos;

import io.github.hylexus.yassos.core.SessionManager;
import io.github.hylexus.yassos.core.UserService;
import io.github.hylexus.yassos.service.BuiltinSessionManagerForDebugging;
import io.github.hylexus.yassos.service.BuiltinUserServiceForDebugging;
import io.github.hylexus.yassos.service.TokenGenerator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

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
        return new BuiltinSessionManagerForDebugging();
    }

    @Bean
    public UserService userService() {
        return new BuiltinUserServiceForDebugging();
    }
}
