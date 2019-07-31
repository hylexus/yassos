package io.github.hylexus.ext.yassos.plugin.user.props;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author hylexus
 * Created At 2019-07-31 22:40
 */
@Getter
@Setter
@ConfigurationProperties(prefix = "yassos.user-store.jdbc")
public class UserStoreJdbcProps {

    private String sqlToLoadUserDetails;

}
