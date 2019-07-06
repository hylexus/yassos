package io.github.hylexus.yassos.support.session;

import io.github.hylexus.yassos.client.model.YassosSession;

/**
 * @author hylexus
 * Created At 2019-06-09 18:35
 */
public interface SessionManager {

    YassosSession getSessionByToken(String token, boolean updateLastAccessTime);

    default YassosSession getSessionByToken(String token) {
        return getSessionByToken(token, false);
    }

    void put(String token, YassosSession yassosSession);

    void removeSessionByToken(String token);
}
