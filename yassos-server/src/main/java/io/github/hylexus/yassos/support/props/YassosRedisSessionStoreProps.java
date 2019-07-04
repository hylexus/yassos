package io.github.hylexus.yassos.support.props;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author hylexus
 * Created At 2019-07-03 22:07
 */
@Setter
@Getter
@ConfigurationProperties(prefix = "yassos.session.redis")
public class YassosRedisSessionStoreProps {
    private String keyPrefix = "yassos_session:";
}
