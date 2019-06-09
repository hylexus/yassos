package io.github.hylexus.yassos.service;

import io.github.hylexus.yassos.client.model.DefaultSessionInfo;
import io.github.hylexus.yassos.client.model.SessionInfo;
import io.github.hylexus.yassos.core.UserService;
import io.github.hylexus.yassos.core.exception.UserAuthException;
import io.github.hylexus.yassos.core.model.LoginForm;
import io.github.hylexus.yassos.entity.UserEntity;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author hylexus
 * Created At 2019-06-09 19:24
 */
public class BuiltinUserServiceForDebugging implements UserService {
    private static final Map<String, UserEntity> USER_STORAGE = new HashMap<>();

    static {
        for (int i = 0; i < 10; i++) {
            UserEntity entity = new UserEntity()
                    .setUserId("id" + i)
                    .setPassword("123456")
                    .setUsername("admin" + i);
            USER_STORAGE.put("admin" + i, entity);
        }
    }

    @Override
    public SessionInfo login(LoginForm form) {
        if (StringUtils.isEmpty(form.getUsername())) {
            throw new UserAuthException("username not exists or password is wrong");
        }
        UserEntity user = USER_STORAGE.get(form.getUsername());
        if (user == null) {
            throw new UserAuthException("username not exists or password is wrong");
        }
        if (!user.getPassword().equals(form.getPassword())) {
            throw new UserAuthException("username not exists or password is wrong");
        }
        return new DefaultSessionInfo()
                .setAuthenticationDate(new Date())
                .setExpiredAt(new Date(new Date().getTime() + 5 * 60 * 1000))
                .setSessionId(null)
                .setUsername(user.getUsername());
    }
}
