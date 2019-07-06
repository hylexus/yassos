package io.github.hylexus.yassos.support.auth;

import io.github.hylexus.yassos.model.UsernamePasswordToken;
import io.github.hylexus.yassos.support.model.UserDetails;
import lombok.NonNull;

import java.util.Objects;

/**
 * @author hylexus
 * Created At 2019-07-04 21:20
 */
public interface CredentialsMatcher {

    boolean match(@NonNull UsernamePasswordToken usernamePasswordToken, @NonNull UserDetails userDetails);

    class PlainTextCredentialsMatcher implements CredentialsMatcher {
        @Override
        public boolean match(UsernamePasswordToken usernamePasswordToken, UserDetails userDetails) {
            return Objects.equals(usernamePasswordToken.getPassword(), userDetails.getPassword());
        }
    }
}
