package io.github.hylexus.yassos.plugin.session;

import io.github.hylexus.yassos.plugin.session.manager.SimpleMemorySessionManager;
import io.github.hylexus.yassos.support.props.session.SessionManagerProps;
import io.github.hylexus.yassos.support.session.SessionManager;
import io.github.hylexus.yassos.support.utils.AnsiUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

import static io.github.hylexus.yassos.support.YassosConfigureConstants.*;
import static io.github.hylexus.yassos.support.utils.AnsiUtils.configParsingTips;

/**
 * @author hylexus
 * Created At 2019-08-03 16:07
 */
@Slf4j
@AutoConfigureOrder(CUSTOM_COMPONENT_ORDER)
@ConditionalOnProperty(prefix = CONFIGURE_KEY_SESSION_MANAGER, name = "type", havingValue = CONFIG_KEY_SESSION_MANAGER_TYPE_MEMORY)
@EnableConfigurationProperties({SessionManagerProps.class})
public class SimpleMemorySessionManagerAutoConfigure {

    @Bean
    @ConditionalOnMissingBean(SessionManager.class)
    private SessionManager sessionManager(SessionManagerProps sessionManagerProps) {
        log.warn(configParsingTips(AnsiUtils.TipsType.WARN,
                "<<Using a component that used only during testing phase : [SimpleMemorySessionManager], "
                        + "please consider to provide your own implementation of SessionManager>>"));
        return new SimpleMemorySessionManager(sessionManagerProps);
    }
}
