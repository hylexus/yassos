package io.github.hylexus.yassos.support.model;

import java.io.Serializable;

/**
 * @author hylexus
 * Created At 2019-07-04 20:01
 */
public interface UserDetails extends Serializable {
    String getUsername();

    String getPassword();

    boolean isLocked();

    boolean isCredentialExpired();

    String getAvatarUrl();
}
