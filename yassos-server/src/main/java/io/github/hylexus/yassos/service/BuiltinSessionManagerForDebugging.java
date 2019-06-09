package io.github.hylexus.yassos.service;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import io.github.hylexus.yassos.client.model.SessionInfo;
import io.github.hylexus.yassos.core.SessionManager;

import java.util.concurrent.TimeUnit;

/**
 * @author hylexus
 * Created At 2019-06-09 18:46
 */
public class BuiltinSessionManagerForDebugging implements SessionManager {
    private Cache<String, SessionInfo> cache = CacheBuilder.newBuilder()
            .expireAfterAccess(5, TimeUnit.MINUTES)
            .recordStats()
            .build();

    @Override
    public SessionInfo getSessionByToken(String token) {
        return cache.getIfPresent(token);
    }

    @Override
    public void put(String token, SessionInfo sessionInfo) {
        cache.put(token, sessionInfo);
    }

    @Override
    public void removeSessionByToken(String token) {
        cache.invalidate(token);
    }
}
