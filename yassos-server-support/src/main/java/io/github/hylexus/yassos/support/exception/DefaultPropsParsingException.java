package io.github.hylexus.yassos.support.exception;

/**
 * @author hylexus
 * Created At 2019-08-06 21:24
 */
public class DefaultPropsParsingException extends RuntimeException {
    public DefaultPropsParsingException() {
    }

    public DefaultPropsParsingException(String message) {
        super(message);
    }

    public DefaultPropsParsingException(String message, Throwable cause) {
        super(message, cause);
    }

    public DefaultPropsParsingException(Throwable cause) {
        super(cause);
    }

    public DefaultPropsParsingException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
