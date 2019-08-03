package io.github.hylexus.yassos.plugin.session;

import io.github.hylexus.yassos.plugin.session.manager.SimpleRedisSessionManager;
import io.github.hylexus.yassos.support.props.session.SessionManagerProps;
import io.github.hylexus.yassos.support.session.SessionManager;
import io.github.hylexus.yassos.support.utils.AnsiUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.RedisTemplate;

import static io.github.hylexus.yassos.support.YassosConfigureConstants.*;
import static io.github.hylexus.yassos.support.utils.AnsiUtils.configParsingTips;

/**
 * @author hylexus
 * Created At 2019-08-03 17:28
 */
@Slf4j
@AutoConfigureOrder(CUSTOM_COMPONENT_ORDER)
@ConditionalOnProperty(prefix = CONFIGURE_KEY_SESSION_MANAGER, name = "type", havingValue = CONFIG_KEY_SESSION_MANAGER_TYPE_REDIS)
@EnableConfigurationProperties({SessionManagerProps.class})
public class SimpleRedisSessionManagerAutoConfigure {

    @Bean
    public SessionManager sessionManager(
            RedisTemplate<String, String> redisTemplate,
            SessionManagerProps sessionManagerProps) {
        log.info(configParsingTips(AnsiUtils.TipsType.INFO, ">>| loading SessionManager [SimpleRedisSessionManager]"));
        return new SimpleRedisSessionManager(redisTemplate, sessionManagerProps);
    }
}
