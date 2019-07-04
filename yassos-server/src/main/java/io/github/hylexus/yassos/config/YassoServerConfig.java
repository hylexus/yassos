package io.github.hylexus.yassos.config;

import io.github.hylexus.yassos.support.props.YassosSessionProps;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author hylexus
 * Created At 2019-07-03 22:00
 */
@Setter
@Getter
@Configuration
@EnableConfigurationProperties({YassosSessionProps.class})
public class YassoServerConfig {
}
