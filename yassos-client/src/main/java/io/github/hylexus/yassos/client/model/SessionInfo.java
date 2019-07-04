package io.github.hylexus.yassos.client.model;

import java.util.Date;

/**
 * @author hylexus
 * Created At 2019-06-07 16:49
 */
public interface SessionInfo {
    String getSessionId();

    SessionInfo setSessionId(String id);

    default String getUserId() {
        return null;
    }

    String getUsername();

    String getAvatarUrl();

    Date getAuthenticationDate();

    Date getExpiredAt();

    default boolean isValid() {
        return getExpiredAt() != null && getExpiredAt().getTime() > System.currentTimeMillis();
    }

    default Object getExtraInfo() {
        return null;
    }

    SessionInfo INVALID_SESSION = new SessionInfo() {
        @Override
        public String getSessionId() {
            return null;
        }

        @Override
        public SessionInfo setSessionId(String id) {
            return this;
        }

        @Override
        public String getUsername() {
            return null;
        }

        @Override
        public String getAvatarUrl() {
            return null;
        }

        @Override
        public Date getAuthenticationDate() {
            return null;
        }

        @Override
        public Date getExpiredAt() {
            return null;
        }

        @Override
        public boolean isValid() {
            return false;
        }

    };
}
