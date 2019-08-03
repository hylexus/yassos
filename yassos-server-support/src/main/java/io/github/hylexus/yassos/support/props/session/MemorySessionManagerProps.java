package io.github.hylexus.yassos.support.props.session;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import static io.github.hylexus.yassos.support.YassosConfigureConstants.CONFIGURE_KEY_SESSION_MANAGER;

/**
 * @author hylexus
 * Created At 2019-08-02 21:07
 */
@Getter
@Setter
@ConfigurationProperties(prefix = CONFIGURE_KEY_SESSION_MANAGER + ".memory")
public class MemorySessionManagerProps {
}
