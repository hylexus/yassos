package io.github.hylexus.yassos.support.props;

import io.github.hylexus.yassos.core.config.ConfigurationKeys;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;

/**
 * @author hylexus
 * Created At 2019-07-12 21:03
 */
@Getter
@Setter
@ConfigurationProperties(prefix = "yassos.cookie")
public class YassosCookieProps {
    private boolean enabled = false;
    private String name = ConfigurationKeys.CONFIG_TOKEN.getDefaultValue();
    private String domain;
    private Duration maxAge = Duration.ofMinutes(30);
    private String path = "/";
    private boolean secure = false;
    private boolean httpOnly = false;
}
