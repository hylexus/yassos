package io.github.hylexus.yassos.core.session;

/**
 * @author hylexus
 * Created At 2019-06-07 16:49
 */
public interface YassosSession {
    String getToken();

    YassosSession setToken(String id);

    String getUsername();

    Long getAuthenticationDate();

    Long getLastAccessTime();

    YassosSession setLastAccessTime(Long lastAccessTime);

    Long getExpiredAt();

    YassosSession setExpiredAt(Long expiredAt);

    default boolean isValid() {
        return getExpiredAt() != null && getExpiredAt() > System.currentTimeMillis();
    }

    YassosSessionAttr getSessionAttr();

    YassosSession INVALID_SESSION = new YassosSession() {
        @Override
        public String getToken() {
            return null;
        }

        @Override
        public YassosSession setToken(String id) {
            return this;
        }

        @Override
        public String getUsername() {
            return null;
        }

        @Override
        public Long getAuthenticationDate() {
            return null;
        }

        @Override
        public Long getLastAccessTime() {
            return null;
        }

        @Override
        public YassosSession setLastAccessTime(Long lastAccessTime) {
            return null;
        }

        @Override
        public Long getExpiredAt() {
            return null;
        }

        @Override
        public YassosSession setExpiredAt(Long expiredAt) {
            return null;
        }

        @Override
        public boolean isValid() {
            return false;
        }

        @Override
        public YassosSessionAttr getSessionAttr() {
            return null;
        }

    };
}
