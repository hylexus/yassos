package io.github.hylexus.yassos.client.repo;

import io.github.hylexus.yassos.client.model.SessionInfo;

/**
 * @author hylexus
 * Created At 2019-06-09 18:13
 */
public interface UserRepository {
    SessionInfo getBySessionId(String token);
}
