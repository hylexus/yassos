package io.github.hylexus.yassos.exception;

import static io.github.hylexus.yassos.config.YassosServerConstant.I18N_AUTH_ACCOUNT_NOT_FOUND;

/**
 * @author hylexus
 * Created At 2019-07-04 20:21
 */
public class AccountNotFoundException extends UserAuthException {

    public AccountNotFoundException(String username) {
        super(username);
    }

    public AccountNotFoundException(String message, String username) {
        super(message, username);
    }

    public AccountNotFoundException(String message, Throwable cause, String username) {
        super(message, cause, username);
    }

    public AccountNotFoundException(Throwable cause, String username) {
        super(cause, username);
    }

    public AccountNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, String username) {
        super(message, cause, enableSuppression, writableStackTrace, username);
    }

    @Override
    public String getI18nCode() {
        return I18N_AUTH_ACCOUNT_NOT_FOUND;
    }
}
