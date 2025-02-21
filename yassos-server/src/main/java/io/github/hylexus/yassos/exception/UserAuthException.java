package io.github.hylexus.yassos.exception;

import io.github.hylexus.yassos.model.I18nCodeSupport;
import lombok.Getter;
import lombok.Setter;

/**
 * @author hylexus
 * Created At 2019-06-09 18:45
 */
@Getter
@Setter
public class UserAuthException extends RuntimeException implements I18nCodeSupport {
    protected String username;

    public UserAuthException(String username) {
        this.username = username;
    }

    public UserAuthException(String message, String username) {
        super(message);
        this.username = username;
    }

    public UserAuthException(String message, Throwable cause, String username) {
        super(message, cause);
        this.username = username;
    }

    public UserAuthException(Throwable cause, String username) {
        super(cause);
        this.username = username;
    }

    public UserAuthException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, String username) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.username = username;
    }

    @Override
    public String getI18nCode() {
        return null;
    }
}
