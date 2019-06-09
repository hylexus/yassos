package io.github.hylexus.yassos.core.repo;

import io.github.hylexus.yassos.core.model.LoginForm;
import io.github.hylexus.yassos.core.model.SessionInfo;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author hylexus
 * Created At 2019-06-07 20:51
 */
public class DefaultRepo implements UserRepository {

    private Map<String, LoginForm> map = new ConcurrentHashMap<>();

    @Override
    public SessionInfo signOn(HttpServletRequest request) {
        return null;
    }

    @Override
    public void signOut(HttpServletRequest request, HttpServletResponse response) {

    }

    @Override
    public boolean isAuthenticated(HttpServletRequest request, HttpServletResponse response) {
        return false;
    }
}
