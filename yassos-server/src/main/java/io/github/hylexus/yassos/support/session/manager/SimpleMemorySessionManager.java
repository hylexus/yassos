package io.github.hylexus.yassos.support.session.manager;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import io.github.hylexus.yassos.core.session.YassosSession;
import io.github.hylexus.yassos.support.props.YassosSessionProps;
import lombok.NonNull;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * @author hylexus
 * Created At 2019-06-09 18:46
 */
public class SimpleMemorySessionManager extends AbstractSessionManager implements InitializingBean {

    @Autowired
    private YassosSessionProps sessionConfig;

    private Cache<String, YassosSession> sessionCache;
    private Cache<String, String> usernameCache;

    @Override
    public Optional<YassosSession> getSessionByToken(String token, boolean updateLastAccessTime) {
        String tokenKey = generateTokenKey(token);
        YassosSession session = sessionCache.getIfPresent(tokenKey);
        if (session == null) {
            return Optional.empty();
        }

        if (updateLastAccessTime) {
            final Long now = System.currentTimeMillis();
            session.setLastAccessTime(now);
            sessionCache.put(tokenKey, session);
            final String usernameKey = generateUsernameKey(session.getUsername());
            usernameCache.put(usernameKey, session.getToken());
        }

        return Optional.of(session);
    }

    @Override
    public void put(String token, YassosSession yassosSession) {
        sessionCache.put(token, yassosSession);
    }

    @Override
    public void removeSessionByToken(String token) {
        sessionCache.invalidate(token);
    }

    @Override
    public Optional<String> getTokenByUsername(@NonNull String username) {
        final String usernameKey = generateUsernameKey(username);
        final String token = usernameCache.getIfPresent(usernameKey);
        if (StringUtils.isBlank(token)) {
            return Optional.empty();
        }
        return Optional.of(token);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        final long ttlInSeconds = sessionConfig.getIdleTime().getSeconds();

        sessionCache = CacheBuilder.newBuilder()
                .expireAfterWrite(ttlInSeconds, TimeUnit.SECONDS)
                .recordStats()
                .build();

        usernameCache = CacheBuilder.newBuilder()
                .expireAfterWrite(ttlInSeconds, TimeUnit.SECONDS)
                .recordStats()
                .build();
    }
}