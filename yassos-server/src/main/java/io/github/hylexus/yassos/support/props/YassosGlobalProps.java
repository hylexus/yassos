package io.github.hylexus.yassos.support.props;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author hylexus
 * Created At 2019-07-12 21:22
 */
@Getter
@Setter
@ConfigurationProperties(prefix = "yassos")
public class YassosGlobalProps {
    private YassosSessionProps session = new YassosSessionProps();
    private YassosCookieProps cookie = new YassosCookieProps();
}
