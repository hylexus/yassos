package io.github.hylexus.ext.yassos.plugin.user;

import io.github.hylexus.ext.yassos.plugin.user.store.SimpleJdbcUserStore;
import io.github.hylexus.yassos.support.props.JdbcUserStoreProps;
import io.github.hylexus.yassos.support.props.UserStoreProps;
import io.github.hylexus.yassos.support.user.store.UserStore;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ansi.AnsiOutput;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;

import static io.github.hylexus.yassos.support.YassosConfigureConstants.*;

/**
 * @author hylexus
 * Created At 2019-07-31 22:44
 */
@Slf4j
@AutoConfigureOrder(CUSTOM_COMPONENT_ORDER)
@ConditionalOnProperty(prefix = CONFIGURE_KEY_USER_STORE, name = "type", havingValue = "jdbc")
@EnableConfigurationProperties({UserStoreProps.class})
public class SimpleJdbcUserStoreAutoConfigure {

    @Autowired
    private UserStoreProps userStoreProps;

    @Bean
    @ConditionalOnMissingBean(UserStore.class)
    public UserStore userDetailService(JdbcTemplate jdbcTemplate) {
        log.info(AnsiOutput.toString(BUILTIN_COMPONENT_COLOR, ">>| Loading userStore [SimpleJdbcUserDetailService]."));
        JdbcUserStoreProps jdbcProps = userStoreProps.getJdbc();
        return new SimpleJdbcUserStore(jdbcTemplate, jdbcProps.getSqlToLoadUserDetails());
    }
}
