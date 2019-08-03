package io.github.hylexus.yassos.plugin.user;

import io.github.hylexus.yassos.plugin.user.loader.SimpleJdbcUserDetailsLoader;
import io.github.hylexus.yassos.support.props.user.store.JdbcUserStoreProps;
import io.github.hylexus.yassos.support.props.user.store.UserStoreProps;
import io.github.hylexus.yassos.support.user.loader.UserDetailsLoader;
import io.github.hylexus.yassos.support.utils.AnsiUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;

import static io.github.hylexus.yassos.support.YassosConfigureConstants.*;
import static io.github.hylexus.yassos.support.utils.AnsiUtils.configParsingTips;

/**
 * @author hylexus
 * Created At 2019-07-31 22:44
 */
@Slf4j
@AutoConfigureOrder(CUSTOM_COMPONENT_ORDER)
@ConditionalOnProperty(prefix = CONFIGURE_KEY_USER_STORE, name = "type", havingValue = CONFIG_KEY_USER_STORE_TYPE_JDBC)
@EnableConfigurationProperties({UserStoreProps.class})
public class SimpleJdbcUserStoreAutoConfigure {

    @Autowired
    private UserStoreProps userStoreProps;

    @Bean
    @ConditionalOnMissingBean(UserDetailsLoader.class)
    public UserDetailsLoader userDetailService(JdbcTemplate jdbcTemplate) {
        log.info(configParsingTips(AnsiUtils.TipsType.INFO, ">>| Loading UserStore [SimpleJdbcUserDetailService]."));
        JdbcUserStoreProps jdbcProps = userStoreProps.getJdbc();
        return new SimpleJdbcUserDetailsLoader(jdbcTemplate, jdbcProps.getSqlToLoadUserDetails());
    }
}
