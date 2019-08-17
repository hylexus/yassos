package io.github.hylexus.yassos.client.handler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author hylexus
 * Created At 2019-07-13 17:24
 */
public interface LogoutHandler {
    /**
     * Callback method before logout
     *
     * @param req   HttpServletRequest
     * @param resp  HttpServletResponse
     * @param token token
     * @throws IOException IOException
     */
    default void preLogout(HttpServletRequest req, HttpServletResponse resp, String token) throws IOException {
    }

    /**
     * Callback method after logout
     *
     * @param req                        HttpServletRequest
     * @param resp                       HttpServletResponse
     * @param token                      token
     * @param tokenDestroyedSuccessfully true if token DESTROYED successfully
     * @throws IOException IOException
     */
    void postLogout(HttpServletRequest req, HttpServletResponse resp, String token, Boolean tokenDestroyedSuccessfully) throws IOException;
}
