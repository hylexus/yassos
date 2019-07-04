package io.github.hylexus.yassos.exception;

/**
 * @author hylexus
 * Created At 2019-07-04 20:21
 */
public class AccountLockedException extends UserAuthException {

    public AccountLockedException(String username) {
        super(username);
    }

    public AccountLockedException(String message, String username) {
        super(message, username);
    }

    public AccountLockedException(String message, Throwable cause, String username) {
        super(message, cause, username);
    }

    public AccountLockedException(Throwable cause, String username) {
        super(cause, username);
    }

    public AccountLockedException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, String username) {
        super(message, cause, enableSuppression, writableStackTrace, username);
    }
}
