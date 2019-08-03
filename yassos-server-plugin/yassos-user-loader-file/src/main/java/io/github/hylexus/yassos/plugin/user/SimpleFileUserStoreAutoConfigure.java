package io.github.hylexus.yassos.plugin.user;

import io.github.hylexus.yassos.plugin.user.loader.SimpleFileUserDetailsLoader;
import io.github.hylexus.yassos.support.props.user.store.FileUserStoreProps;
import io.github.hylexus.yassos.support.props.user.store.UserStoreProps;
import io.github.hylexus.yassos.support.user.loader.UserDetailsLoader;
import io.github.hylexus.yassos.support.utils.AnsiUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

import static io.github.hylexus.yassos.support.YassosConfigureConstants.*;
import static io.github.hylexus.yassos.support.utils.AnsiUtils.configParsingTips;

/**
 * @author hylexus
 * Created At 2019-08-01 23:40
 */
@Slf4j
@AutoConfigureOrder(CUSTOM_COMPONENT_ORDER)
@ConditionalOnProperty(prefix = CONFIGURE_KEY_USER_STORE, name = "type", havingValue = CONFIG_KEY_USER_STORE_TYPE_FILE)
@EnableConfigurationProperties({UserStoreProps.class})
public class SimpleFileUserStoreAutoConfigure {

    @Autowired
    private UserStoreProps userStoreProps;

    @Bean
    public UserDetailsLoader userStore() {
        FileUserStoreProps fileProps = userStoreProps.getFile();
        log.warn(configParsingTips(AnsiUtils.TipsType.WARN, "<<Using a component that used only during testing phase : [SimpleFileUserDetailsLoader], please consider to provide your own implementation of UserDetailsLoader>>"), fileProps.getFileLocation());
        log.info(configParsingTips(AnsiUtils.TipsType.WARN, ">>| Loading UserDetails [SimpleFileUserStore] from : '{}'"), fileProps.getFileLocation());
        return new SimpleFileUserDetailsLoader(fileProps.getFileLocation());
    }
}
