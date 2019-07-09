package io.github.hylexus.yassos.client.repo;

import io.github.hylexus.yassos.core.session.YassosSession;

/**
 * @author hylexus
 * Created At 2019-06-09 18:13
 */
public interface UserRepository {
    YassosSession getBySessionId(String token);
}
