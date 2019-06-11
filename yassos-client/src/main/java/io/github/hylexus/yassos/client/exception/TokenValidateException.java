package io.github.hylexus.yassos.client.exception;

/**
 * @author hylexus
 * Created At 2019-06-11 22:51
 */
public class TokenValidateException extends RuntimeException {
    public TokenValidateException() {
    }

    public TokenValidateException(String message) {
        super(message);
    }

    public TokenValidateException(String message, Throwable cause) {
        super(message, cause);
    }

    public TokenValidateException(Throwable cause) {
        super(cause);
    }

    public TokenValidateException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
