package io.github.hylexus.yassos.core.repo;

import io.github.hylexus.yassos.core.model.SessionInfo;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author hylexus
 * Created At 2019-06-07 16:50
 */
public interface UserRepository {

    SessionInfo signOn(HttpServletRequest request);

    void signOut(HttpServletRequest request, HttpServletResponse response);

    boolean isAuthenticated(HttpServletRequest request, HttpServletResponse response);
}
