package io.github.hylexus.yassos.config;

import org.springframework.boot.ansi.AnsiColor;

/**
 * @author hylexus
 * Created At 2019-07-16 22:39
 */
public interface YassosServerConstant {

    String PARAM_KEY_AUTH_ERR_MSG_KEY = "auth_err_msg_key";
    String PARAM_KEY_AUTH_ERR_MSG_KEY_118N = "auth_err_msg_i18n_code";
    String PARAM_KEY_REDIRECT_URL_NAME = "redirect_url_name";
    String PARAM_KEY_REDIRECT_URL_VALUE = "redirect_url_value";

    String I18N_AUTH_ACCOUNT_NOT_FOUND = "login.auth.account-not-found";
    String I18N_AUTH_ACCOUNT_LOCKED = "login.auth.account-locked";
    String I18N_AUTH_CREDENTIAL_EXPIRED = "login.auth.credential-expired";
    String I18N_AUTH_BAD_CREDENTIALS = "login.auth.bad-credentials";

    AnsiColor COLOR_CONFIG_PARSE_TIPS = AnsiColor.BRIGHT_GREEN;
    String CONFIG_KEY_YASSOS_HOME = "yassos.home";
    String CONFIG_KEY_YASSOS_LIB_EXT_DIR = "ext";

}
