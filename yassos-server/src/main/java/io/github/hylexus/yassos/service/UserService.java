package io.github.hylexus.yassos.service;

import io.github.hylexus.yassos.exception.*;
import io.github.hylexus.yassos.support.auth.CredentialsMatcher;
import io.github.hylexus.yassos.support.model.UserDetails;
import io.github.hylexus.yassos.support.model.UsernamePasswordToken;
import io.github.hylexus.yassos.support.user.loader.UserDetailsLoader;
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
    private UserDetailsLoader userDetailsLoader;

    @Autowired
    private CredentialsMatcher credentialsMatcher;

    @Autowired
    private LocaleMessage localeMessage;

    public UserDetails login(@NonNull UsernamePasswordToken usernamePasswordToken) throws UserAuthException {
        final String username = usernamePasswordToken.getUsername();
        final UserDetails user = this.userDetailsLoader.loadByUsername(username);
        if (user == null) {
            throw new AccountNotFoundException(localeMessage.getMessage("login.auth.account-not-found"), username);
        }

        if (user.isLocked()) {
            throw new AccountLockedException(localeMessage.getMessage("login.auth.account-locked"), username);
        }

        if (user.isCredentialExpired()) {
            throw new AccountCredentialExpiredException(localeMessage.getMessage("login.auth.credential-expired"), username);
        }

        if (!this.credentialsMatcher.match(usernamePasswordToken, user)) {
            throw new BadCredentialsException(localeMessage.getMessage("login.auth.bad-credentials"), username);
        }

        return user;
    }
}
