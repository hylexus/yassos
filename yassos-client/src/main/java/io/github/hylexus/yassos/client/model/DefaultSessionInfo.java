package io.github.hylexus.yassos.client.model;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * @author hylexus
 * Created At 2019-06-09 16:27
 */
@Getter
@Setter
@Accessors(chain = true)
public class DefaultSessionInfo implements SessionInfo {
    private String sessionId;
    private String username;
    private Date authenticationDate;
    private Date expiredAt;

    @Override
    public String sessionId() {
        return sessionId;
    }

    @Override
    public void sessionId(String id) {
        setSessionId(id);
    }

    @Override
    public String username() {
        return username;
    }

    @Override
    public Date authenticationDate() {
        return authenticationDate;
    }

    @Override
    public Date expiredAt() {
        return expiredAt;
    }
}
