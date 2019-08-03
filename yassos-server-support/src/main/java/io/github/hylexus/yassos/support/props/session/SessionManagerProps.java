package io.github.hylexus.yassos.support.props.session;

import io.github.hylexus.yassos.support.session.SessionManagerType;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;

import static io.github.hylexus.yassos.support.YassosConfigureConstants.CONFIGURE_KEY_SESSION_MANAGER;

/**
 * @author hylexus
 * Created At 2019-08-02 21:04
 */
@Getter
@Setter
@ConfigurationProperties(prefix = CONFIGURE_KEY_SESSION_MANAGER)
public class SessionManagerProps {

    private SessionManagerType type = SessionManagerType.MEMORY;

    private Duration idleTime = Duration.ofMinutes(30);

    private MemorySessionManagerProps memory = new MemorySessionManagerProps();
    private RedisManagerProps redis = new RedisManagerProps();

}
