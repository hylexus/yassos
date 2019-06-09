package io.github.hylexus.yassos.core.exception;

/**
 * @author hylexus
 * Created At 2019-06-09 18:45
 */
public class UserAuthException extends RuntimeException {
    public UserAuthException() {
    }

    public UserAuthException(String message) {
        super(message);
    }

    public UserAuthException(String message, Throwable cause) {
        super(message, cause);
    }

    public UserAuthException(Throwable cause) {
        super(cause);
    }

    public UserAuthException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
