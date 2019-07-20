package io.github.hylexus.yassos.custom;

import io.github.hylexus.yassos.config.BuiltinYassosServerConfig;
import io.github.hylexus.yassos.support.auth.user.SimpleJdbcUserDetailService;
import io.github.hylexus.yassos.support.auth.user.UserDetailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * @author hylexus
 * Created At 2019-07-10 22:15
 */
@Slf4j
@Configuration
public class MyYassosServerConfig extends BuiltinYassosServerConfig {

//    @Bean
    public UserDetailService userDetailService(JdbcTemplate jdbcTemplate) {
        String sqlToLoadUserDetails = "select id   as userId,\n" +
                "       name as username,\n" +
                "       password,\n" +
                "       locked,\n" +
                "       credential_expired,\n" +
                "       avatarUrl\n" +
                "from yassos_user\n" +
                "where name = ?";
        return new SimpleJdbcUserDetailService(jdbcTemplate, sqlToLoadUserDetails);
    }

}
