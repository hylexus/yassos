package io.github.hylexus.yassos.support.exception;

/**
 * @author hylexus
 * Created At 2019-08-02 00:06
 */
public class PluginLoadingException extends RuntimeException {
    public PluginLoadingException() {
    }

    public PluginLoadingException(String message) {
        super(message);
    }

    public PluginLoadingException(String message, Throwable cause) {
        super(message, cause);
    }

    public PluginLoadingException(Throwable cause) {
        super(cause);
    }

    public PluginLoadingException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
