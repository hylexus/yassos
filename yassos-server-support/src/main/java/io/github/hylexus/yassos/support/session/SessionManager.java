package io.github.hylexus.yassos.support.session;

import io.github.hylexus.yassos.core.session.YassosSession;
import lombok.NonNull;

import java.util.Optional;

/**
 * @author hylexus
 * Created At 2019-06-09 18:35
 */
public interface SessionManager {

    Optional<YassosSession> getSessionByToken(@NonNull String token, boolean updateLastAccessTime);

    default Optional<YassosSession> getSessionByToken(@NonNull String token) {
        return getSessionByToken(token, false);
    }

    default Optional<YassosSession> getSessionByUsername(@NonNull String username, boolean updateLastAccessTime) {
        Optional<String> tokenInfo = this.getTokenByUsername(username);
        if (!tokenInfo.isPresent()) {
            return Optional.empty();
        }

        return this.getSessionByToken(tokenInfo.get(), updateLastAccessTime);
    }

    default Optional<YassosSession> getSessionByUsername(@NonNull String username) {
        return getSessionByUsername(username, false);
    }

    void put(@NonNull String token, @NonNull YassosSession yassosSession);

    void removeSessionByToken(@NonNull String token);

    Optional<String> getTokenByUsername(@NonNull String username);
}
