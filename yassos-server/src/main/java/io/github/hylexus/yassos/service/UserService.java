package io.github.hylexus.yassos.service;

import io.github.hylexus.yassos.core.model.UsernamePasswordToken;
import io.github.hylexus.yassos.exception.*;
import io.github.hylexus.yassos.support.auth.CredentialsMatcher;
import io.github.hylexus.yassos.support.model.UserDetails;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author hylexus
 * Created At 2019-07-04 20:19
 */
@Component
public class UserService {

    @Autowired
    private UserDetailService userDetailService;

    @Autowired
    private CredentialsMatcher credentialsMatcher;

    public UserDetails login(@NonNull UsernamePasswordToken usernamePasswordToken) throws UserAuthException {
        String username = usernamePasswordToken.getUsername();
        UserDetails user = this.userDetailService.loadByUsername(username);
        if (user == null) {
            throw new AccountNotFoundException(username);
        }

        if (user.isLocked()) {
            throw new AccountLockedException(username);
        }

        if (user.isCredentialExpired()) {
            throw new AccountCredentialExpiredException(username);
        }

        if (!this.credentialsMatcher.match(usernamePasswordToken, user)) {
            throw new BadCredentialsException(username);
        }

        return user;
    }
}
