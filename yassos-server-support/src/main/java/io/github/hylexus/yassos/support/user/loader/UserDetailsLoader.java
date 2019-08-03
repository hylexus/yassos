package io.github.hylexus.yassos.support.user.loader;

import io.github.hylexus.yassos.support.model.UserDetails;

/**
 * @author hylexus
 * Created At 2019-07-04 20:12
 */
public interface UserDetailsLoader {

    UserDetails loadByUsername(String username);
}
