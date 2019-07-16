package io.github.hylexus.yassos.service;

import io.github.hylexus.yassos.exception.*;
import io.github.hylexus.yassos.model.UsernamePasswordToken;
import io.github.hylexus.yassos.support.auth.CredentialsMatcher;
import io.github.hylexus.yassos.support.auth.user.UserDetailService;
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
        final String username = usernamePasswordToken.getUsername();
        final UserDetails user = this.userDetailService.loadByUsername(username);
        if (user == null) {
            throw new AccountNotFoundException("AccountNotFound", username);
        }

        if (user.isLocked()) {
            throw new AccountLockedException("AccountLocked", username);
        }

        if (user.isCredentialExpired()) {
            throw new AccountCredentialExpiredException("CredentialExpired", username);
        }

        if (!this.credentialsMatcher.match(usernamePasswordToken, user)) {
            throw new BadCredentialsException("Bad Credentials", username);
        }

        return user;
    }
}
