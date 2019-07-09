package io.github.hylexus.yassos.support.session.manager;

import io.github.hylexus.yassos.support.props.YassosSessionProps;
import io.github.hylexus.yassos.support.session.YassosSessionAttrConverter;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author hylexus
 * Created At 2019-07-09 21:41
 */
public abstract class AbstractSessionManager implements SessionManager {

    @Autowired
    protected YassosSessionProps sessionProps;

    @Autowired
    protected YassosSessionAttrConverter sessionAttrConverter;


    protected String generateTokenKey(String token) {
        return sessionProps.getRedis().getKeyPrefix() + token;
    }

    protected String generateUsernameKey(String username) {
        return sessionProps.getRedis().getKeyPrefix() + username;
    }

}
