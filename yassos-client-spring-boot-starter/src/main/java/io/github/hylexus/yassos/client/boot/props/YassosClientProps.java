package io.github.hylexus.yassos.client.boot.props;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;

/**
 * @author hylexus
 * Created At 2019-07-12 22:04
 */
@Getter
@Setter
@ToString
@ConfigurationProperties(prefix = "yassos.client")
public class YassosClientProps {
    private boolean enable = false;
    private List<String> ignoreUriPatterns = new ArrayList<>();
    private String logoutUri = "/logout";
    private boolean encodeUrl = false;
    private boolean throwExceptionIfTokenValidateException = true;
    private boolean useSession = false;
    private String sessionKey = "yassos_session_key";
}
