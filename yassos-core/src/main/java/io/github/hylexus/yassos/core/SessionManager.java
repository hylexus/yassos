package io.github.hylexus.yassos.core;

import io.github.hylexus.yassos.client.model.SessionInfo;

/**
 * @author hylexus
 * Created At 2019-06-09 18:35
 */
public interface SessionManager {

    SessionInfo getSessionByToken(String token);

    void put(String token, SessionInfo sessionInfo);

    void removeSessionByToken(String token);
}
