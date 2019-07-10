package io.github.hylexus.yassos.exception;

/**
 * @author hylexus
 * Created At 2019-07-04 20:23
 */
public class AccountCredentialExpiredException extends UserAuthException {
    public AccountCredentialExpiredException(String username) {
        super(username);
    }

    public AccountCredentialExpiredException(String message, String username) {
        super(message, username);
    }

    public AccountCredentialExpiredException(String message, Throwable cause, String username) {
        super(message, cause, username);
    }

    public AccountCredentialExpiredException(Throwable cause, String username) {
        super(cause, username);
    }

    public AccountCredentialExpiredException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, String username) {
        super(message, cause, enableSuppression, writableStackTrace, username);
    }
}