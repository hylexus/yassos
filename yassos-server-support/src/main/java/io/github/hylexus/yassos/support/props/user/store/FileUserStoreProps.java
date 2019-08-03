package io.github.hylexus.yassos.support.props.user.store;

import io.github.hylexus.yassos.support.YassosConfigureConstants;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author hylexus
 * Created At 2019-08-01 23:45
 */
@Getter
@Setter
@ConfigurationProperties(prefix = YassosConfigureConstants.CONFIGURE_KEY_USER_STORE + ".file")
public class FileUserStoreProps {
    private String fileLocation;
}
