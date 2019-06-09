package io.github.hylexus.yassos.core;

import io.github.hylexus.yassos.client.model.SessionInfo;
import io.github.hylexus.yassos.core.exception.UserAuthException;
import io.github.hylexus.yassos.core.model.LoginForm;

/**
 * @author hylexus
 * Created At 2019-06-09 19:23
 */
public interface UserService {

    SessionInfo login(LoginForm form) throws UserAuthException;
}
