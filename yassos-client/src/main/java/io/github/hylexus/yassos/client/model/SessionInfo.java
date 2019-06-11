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

    SessionInfo INVALID_SESSION = new SessionInfo() {
        @Override
        public String sessionId() {
            return null;
        }

        @Override
        public void sessionId(String id) {

        }

        @Override
        public boolean isValid() {
            return false;
        }

        @Override
        public String username() {
            return null;
        }

        @Override
        public Date authenticationDate() {
            return null;
        }

        @Override
        public Date expiredAt() {
            return null;
        }
    };
}
