package io.github.hylexus.yassos.client.handler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author hylexus
 * Created At 2019-07-13 17:24
 */
public interface LogoutHandler {
    default void preLogout(HttpServletRequest req, HttpServletResponse resp, String token) throws IOException {
    }

    void postLogout(HttpServletRequest req, HttpServletResponse resp, String token, Boolean tokenDestroyedSuccessfully) throws IOException;
}
