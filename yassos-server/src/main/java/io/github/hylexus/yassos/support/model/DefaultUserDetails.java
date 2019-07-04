package io.github.hylexus.yassos.support.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * @author hylexus
 * Created At 2019-07-04 20:05
 */
@Getter
@Setter
@Accessors(chain = true)
@ToString
public class DefaultUserDetails implements UserDetails {
    private Long userId;
    private String username;
    private String password;
    private boolean locked;
    private boolean CredentialExpired;
    private String avatarUrl;
}
