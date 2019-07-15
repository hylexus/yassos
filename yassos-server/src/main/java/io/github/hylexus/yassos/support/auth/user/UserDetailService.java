package io.github.hylexus.yassos.support.auth.user;

import io.github.hylexus.yassos.support.model.UserDetails;

/**
 * @author hylexus
 * Created At 2019-07-04 20:12
 */
public interface UserDetailService {

    UserDetails loadByUsername(String username);
}
