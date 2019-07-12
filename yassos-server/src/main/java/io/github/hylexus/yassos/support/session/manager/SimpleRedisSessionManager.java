package io.github.hylexus.yassos.support.session.manager;

import io.github.hylexus.yassos.core.session.YassosSession;
import io.github.hylexus.yassos.core.session.YassosSessionAttr;
import io.github.hylexus.yassos.support.session.SimpleYassosSession;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.CollectionUtils;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * <pre>
 *  {
 *      "prefix:token" : "sessionInfoBasedOnJSON",
 *      "prefix:username" : "token"
 *  }
 * </pre>
 *
 * @author hylexus
 * Created At 2019-07-03 22:02
 */
@Slf4j
public class SimpleRedisSessionManager extends AbstractSessionManager {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    private static final String HASH_KEY_USERNAME = "username";
    private static final String HASH_KEY_TOKEN = "token";
    private static final String HASH_KEY_LAST_ACCESS_TIME = "last_access";
    private static final String HASH_KEY_CREATED_AT = "created_at";
    private static final String HASH_KEY_SESSION_ATTR = "attr";

    @Override
    public Optional<YassosSession> getSessionByToken(String token, boolean updateLastAccessTime) {
        final String tokenKey = generateTokenKey(token);

        final SimpleYassosSession session = new SimpleYassosSession();
        final Map<Object, Object> entries = redisTemplate.opsForHash().entries(tokenKey);
        if (CollectionUtils.isEmpty(entries)) {
            return Optional.empty();
        }

        session.setUsername(entries.getOrDefault(HASH_KEY_USERNAME, "").toString());
        session.setToken(entries.getOrDefault(HASH_KEY_TOKEN, "").toString());

        session.setAuthenticationDate(string2Date(entries.getOrDefault(HASH_KEY_CREATED_AT, "").toString()));
        session.setLastAccessTime(string2Date(entries.getOrDefault(HASH_KEY_LAST_ACCESS_TIME, "").toString()));

        final YassosSessionAttr sessionAttr = sessionAttrConverter.fromString(entries.getOrDefault(HASH_KEY_SESSION_ATTR, "{}").toString());
        session.setSessionAttr(sessionAttr);

        if (updateLastAccessTime) {
            final Long now = System.currentTimeMillis();
            session.setLastAccessTime(now);

            redisTemplate.opsForHash().put(tokenKey, HASH_KEY_LAST_ACCESS_TIME, date2String(now));
            ttl(tokenKey);

            final String usernameKey = generateUsernameKey(session.getUsername());
            ttl(usernameKey);
        }

        final Long expire = redisTemplate.getExpire(tokenKey, TimeUnit.SECONDS);
        if (expire != null) {
            session.setExpiredAt(session.getLastAccessTime() + expire);
        }

        return Optional.of(session);
    }

    @Override
    public Optional<String> getTokenByUsername(@NonNull String username) {
        final String usernameKey = generateUsernameKey(username);
        final String token = redisTemplate.opsForValue().get(usernameKey);

        if (StringUtils.isBlank(token)) {
            return Optional.empty();
        }

        return Optional.of(token);
    }

    @Override
    public void put(String token, YassosSession yassosSession) {
        final String tokenKey = generateTokenKey(token);
        final String usernameKey = generateUsernameKey(yassosSession.getUsername());

        redisTemplate.opsForValue().set(usernameKey, token);

        redisTemplate.opsForHash().put(tokenKey, HASH_KEY_USERNAME, yassosSession.getUsername());
        redisTemplate.opsForHash().put(tokenKey, HASH_KEY_TOKEN, yassosSession.getToken());

        redisTemplate.opsForHash().put(tokenKey, HASH_KEY_LAST_ACCESS_TIME, date2String(yassosSession.getLastAccessTime()));
        redisTemplate.opsForHash().put(tokenKey, HASH_KEY_CREATED_AT, date2String(yassosSession.getAuthenticationDate()));

        if (yassosSession.getSessionAttr() != null) {
            redisTemplate.opsForHash().put(tokenKey, HASH_KEY_SESSION_ATTR, sessionAttrConverter.toString(yassosSession.getSessionAttr()));
        }

        ttl(usernameKey);
        ttl(tokenKey);
    }

    @Override
    public void removeSessionByToken(@NonNull String token) {
        redisTemplate.delete(generateTokenKey(token));
        redisTemplate.delete(generateUsernameKey(token));
    }

    private String date2String(Long localDateTime) {
        return String.valueOf(localDateTime);
    }

    private Long string2Date(String str) {
        if (StringUtils.isEmpty(str))
            return null;
        return Long.valueOf(str);
    }

    private void ttl(String key) {
        redisTemplate.expire(key, globalProps.getSession().getIdleTime().getSeconds(), TimeUnit.SECONDS);
    }
}
