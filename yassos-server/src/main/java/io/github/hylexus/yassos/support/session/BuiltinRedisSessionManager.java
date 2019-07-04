package io.github.hylexus.yassos.support.session;

import com.alibaba.fastjson.JSON;
import io.github.hylexus.yassos.client.model.DefaultSessionInfo;
import io.github.hylexus.yassos.client.model.SessionInfo;
import io.github.hylexus.yassos.support.props.YassosSessionProps;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

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
public class BuiltinRedisSessionManager implements SessionManager {

    @Autowired
    private YassosSessionProps sessionProps;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Override
    public SessionInfo getSessionByToken(String token) {
        String str = redisTemplate.opsForValue().get(generateTokenKey(token));
        if (StringUtils.isEmpty(str)) {
            log.debug("session is null. token: {}", token);
            return null;
        }
        return JSON.parseObject(str, DefaultSessionInfo.class);
    }

    @Override
    public void put(String token, SessionInfo sessionInfo) {
        String tokenKey = generateTokenKey(token);
        String usernameKey = generateUsernameKey(sessionInfo.getUsername());
        redisTemplate.opsForValue().set(usernameKey, token, sessionProps.getIdleTime().getSeconds(), TimeUnit.SECONDS);
        redisTemplate.opsForValue().set(tokenKey, JSON.toJSONString(sessionInfo), sessionProps.getIdleTime().getSeconds(), TimeUnit.SECONDS);
    }

    @Override
    public void removeSessionByToken(String token) {
        redisTemplate.delete(generateTokenKey(token));
        redisTemplate.delete(generateUsernameKey(token));
    }

    private String generateTokenKey(String token) {
        return sessionProps.getRedis().getKeyPrefix() + token;
    }

    private String generateUsernameKey(String username) {
        return sessionProps.getRedis().getKeyPrefix() + username;
    }

}
