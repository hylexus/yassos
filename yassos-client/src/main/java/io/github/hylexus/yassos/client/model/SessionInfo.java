package io.github.hylexus.yassos.client.model;

import java.util.Date;

/**
 * @author hylexus
 * Created At 2019-06-07 16:49
 */
public interface SessionInfo {
    String sessionId();

    void sessionId(String id);

    String username();

    Date authenticationDate();

    Date expiredAt();

    default boolean isValid() {
        return expiredAt() != null && expiredAt().getTime() > System.currentTimeMillis();
    }

    default Object extra() {
        return null;
    }
}
