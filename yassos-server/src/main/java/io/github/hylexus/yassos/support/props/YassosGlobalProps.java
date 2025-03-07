package io.github.hylexus.yassos.support.props;

import io.github.hylexus.yassos.support.props.session.SessionManagerProps;
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
    private SessionManagerProps sessionManager;
    private YassosCookieProps clientCookie = new YassosCookieProps();
}
