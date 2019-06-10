package io.github.hylexus.yassos.client.config;

import io.github.hylexus.yassos.client.model.SessionInfo;

/**
 * @author hylexus
 * Created At 2019-06-10 21:57
 */
@FunctionalInterface
public interface TokenValidator {
    /**
     * @param token ticket
     * @param url   the target url to access with this {@code ticket}
     * @return current session info if
     */
    SessionInfo validateToken(String token, String url);
}
