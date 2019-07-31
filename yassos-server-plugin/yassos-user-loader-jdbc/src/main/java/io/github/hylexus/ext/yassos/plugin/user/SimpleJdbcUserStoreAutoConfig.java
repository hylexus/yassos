package io.github.hylexus.ext.yassos.plugin.user;

import io.github.hylexus.ext.yassos.plugin.user.props.UserStoreJdbcProps;
import io.github.hylexus.ext.yassos.plugin.user.store.SimpleJdbcUserDetailService;
import io.github.hylexus.yassos.support.auth.UserDetailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;

import static org.springframework.core.Ordered.HIGHEST_PRECEDENCE;

/**
 * @author hylexus
 * Created At 2019-07-31 22:44
 */
@Slf4j
@AutoConfigureOrder(HIGHEST_PRECEDENCE + 10)
@EnableConfigurationProperties({UserStoreJdbcProps.class})
public class SimpleJdbcUserStoreAutoConfig {

    @Autowired
    private UserStoreJdbcProps jdbcProps;

    @Bean
    @ConditionalOnMissingBean(UserDetailService.class)
    public UserDetailService userDetailService(JdbcTemplate jdbcTemplate) {
        log.info(">>| load SimpleJdbcUserDetailService.");
        return new SimpleJdbcUserDetailService(jdbcTemplate, jdbcProps.getSqlToLoadUserDetails());
    }
}
