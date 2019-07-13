package io.github.hylexus.yassos.client.boot.props;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author hylexus
 * Created At 2019-07-12 22:17
 */
@Getter
@Setter
@ToString
@ConfigurationProperties(prefix = "yassos.server")
public class YassosServerProps {
    private String serverUrlPrefix;
    private String signOnUrl = serverUrlPrefix + "/login";
}
