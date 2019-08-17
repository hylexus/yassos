package io.github.hylexus.yassos.client.session;

import io.github.hylexus.yassos.client.exception.TokenValidateException;
import io.github.hylexus.yassos.core.session.YassosSession;

/**
 * @author hylexus
 * Created At 2019-06-10 21:57
 */
public interface SessionInfoAccessor {
    /**
     * Fetch session info from yassos-server
     *
     * @param token ticket
     * @param url   the target url to <b>fetch</b> information about this {@code token} (ticket) .
     * @return current session information
     * @throws TokenValidateException throw an exception if token validation fails.
     */
    YassosSession fetchSessionInfo(String token, String url) throws TokenValidateException;

    /**
     * DESTROY a token (logout)
     *
     * @param token ticket
     * @param url   the target url to <b>destroy</b> this {@code token} (ticket)
     * @return true if the token was destroyed successfully
     * @throws TokenValidateException throw an exception if the communication to SSO-server is abnormal or other abnormalities occur
     */
    boolean destroyToken(String token, String url) throws TokenValidateException;

}
