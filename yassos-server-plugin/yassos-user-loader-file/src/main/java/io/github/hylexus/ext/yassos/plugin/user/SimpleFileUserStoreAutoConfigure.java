package io.github.hylexus.ext.yassos.plugin.user;

import io.github.hylexus.ext.yassos.plugin.user.store.SimpleFileUserStore;
import io.github.hylexus.yassos.support.props.FileUserStoreProps;
import io.github.hylexus.yassos.support.props.UserStoreProps;
import io.github.hylexus.yassos.support.user.store.UserStore;
import io.github.hylexus.yassos.support.utils.AnsiUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

import static io.github.hylexus.yassos.support.YassosConfigureConstants.CONFIGURE_KEY_USER_STORE;
import static io.github.hylexus.yassos.support.YassosConfigureConstants.CUSTOM_COMPONENT_ORDER;
import static io.github.hylexus.yassos.support.utils.AnsiUtils.configParsingTips;

/**
 * @author hylexus
 * Created At 2019-08-01 23:40
 */
@Slf4j
@AutoConfigureOrder(CUSTOM_COMPONENT_ORDER)
@ConditionalOnProperty(prefix = CONFIGURE_KEY_USER_STORE, name = "type", havingValue = "file")
@EnableConfigurationProperties({UserStoreProps.class})
public class SimpleFileUserStoreAutoConfigure {

    @Autowired
    private UserStoreProps userStoreProps;

    @Bean
    public UserStore userStore() {
        FileUserStoreProps fileProps = userStoreProps.getFile();
        log.info(configParsingTips(AnsiUtils.TipsType.INFO, ">>| Loading UserDetails [SimpleFileUserStore] from file : '{}'"), fileProps.getFileLocation());
        return new SimpleFileUserStore(fileProps.getFileLocation());
    }
}
