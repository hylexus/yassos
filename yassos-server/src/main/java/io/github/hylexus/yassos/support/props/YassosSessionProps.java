package io.github.hylexus.yassos.support.props;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;

/**
 * @author hylexus
 * Created At 2019-07-03 21:42
 */
@Setter
@Getter
@ConfigurationProperties(prefix = "yassos.session")
public class YassosSessionProps {

    private Duration idleTime = Duration.ofMinutes(10);
    private YassosRedisSessionStoreProps redis = new YassosRedisSessionStoreProps();
}
