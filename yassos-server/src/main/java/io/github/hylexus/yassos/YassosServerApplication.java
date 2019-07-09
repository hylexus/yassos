package io.github.hylexus.yassos;

import io.github.hylexus.yassos.service.UserDetailService;
import io.github.hylexus.yassos.support.model.DefaultUserDetails;
import io.github.hylexus.yassos.support.model.UserDetails;
import io.github.hylexus.yassos.support.session.manager.SessionManager;
import io.github.hylexus.yassos.support.session.manager.SimpleRedisSessionManager;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
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
    public SessionManager sessionManager() {
        return new SimpleRedisSessionManager();
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
                        .setCredentialExpired(false)
                        .setAvatarUrl("https://static.my-server.com/avatar/" + username + ".png");
            }
        };
    }

}
