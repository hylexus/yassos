package io.github.hylexus.yassos.support.session.manager;

import io.github.hylexus.yassos.support.props.YassosGlobalProps;
import io.github.hylexus.yassos.support.session.SessionManager;
import io.github.hylexus.yassos.support.session.enhance.YassosSessionAttrConverter;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author hylexus
 * Created At 2019-07-09 21:41
 */
public abstract class AbstractSessionManager implements SessionManager {

    @Autowired
    protected YassosGlobalProps globalProps;

    @Autowired
    protected YassosSessionAttrConverter sessionAttrConverter;


    protected String generateTokenKey(String token) {
        return globalProps.getSession().getRedis().getKeyPrefix() + token;
    }

    protected String generateUsernameKey(String username) {
        return globalProps.getSession().getRedis().getKeyPrefix() + username;
    }

}
