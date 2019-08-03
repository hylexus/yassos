package io.github.hylexus.yassos.support.props.user.store;

import io.github.hylexus.yassos.support.user.store.UserStoreType;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import static io.github.hylexus.yassos.support.YassosConfigureConstants.CONFIGURE_KEY_USER_STORE;

/**
 * @author hylexus
 * Created At 2019-08-01 23:29
 */
@Getter
@Setter
@ConfigurationProperties(prefix = CONFIGURE_KEY_USER_STORE)
public class UserStoreProps {

    private UserStoreType type = UserStoreType.FILE;

    private JdbcUserStoreProps jdbc = new JdbcUserStoreProps();
    private FileUserStoreProps file = new FileUserStoreProps();
}
