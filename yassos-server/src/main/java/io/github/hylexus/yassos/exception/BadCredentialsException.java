package io.github.hylexus.yassos.exception;

/**
 * @author hylexus
 * Created At 2019-07-04 20:21
 */
public class BadCredentialsException extends UserAuthException {

    public BadCredentialsException(String username) {
        super(username);
    }

    public BadCredentialsException(String message, String username) {
        super(message, username);
    }

    public BadCredentialsException(String message, Throwable cause, String username) {
        super(message, cause, username);
    }

    public BadCredentialsException(Throwable cause, String username) {
        super(cause, username);
    }

    public BadCredentialsException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, String username) {
        super(message, cause, enableSuppression, writableStackTrace, username);
    }
}
