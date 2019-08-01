package io.github.hylexus.yassos.support.auth.user;

import io.github.hylexus.yassos.support.model.DefaultUserDetails;
import io.github.hylexus.yassos.support.model.UserDetails;
import io.github.hylexus.yassos.support.user.store.UserStore;

import java.util.Random;

/**
 * @author hylexus
 * Created At 2019-07-14 00:05
 */
@Deprecated
public class BuiltinUserServiceForDebugging implements UserStore {
    @Override
    public UserDetails loadByUsername(String username) {
        return new DefaultUserDetails()
                .setUsername(username)
                .setPassword("1234561")
                .setUserId(new Random().nextLong())
                .setLocked(false)
                .setCredentialExpired(false)
                .setAvatarUrl("https://static.my-server.com/avatar/" + username + ".png");
    }
}
