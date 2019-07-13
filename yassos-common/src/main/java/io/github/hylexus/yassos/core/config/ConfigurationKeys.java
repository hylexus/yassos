package io.github.hylexus.yassos.core.config;

import static io.github.hylexus.yassos.core.config.ConfigurationKey.ConfigurationKeyValidator.NOT_EMPTY;

/**
 * @author hylexus
 * Created At 2019-06-07 20:14
 */
public interface ConfigurationKeys {
    String CALLBACK_ADDRESS_NAME = "redirect_url";
    String URI_PATTERNS_SEPARATOR = ",";

    String SSO_SERVER_TOKEN_VALIDATE_URI = "/validate";
    String SSO_SERVER_TOKEN_DESTROY_URI = "/sign-out";

    ConfigurationKey<String> CONFIG_SSO_SERVER_LOGIN_URL = new ConfigurationKey<>("ssoServerLoginUrl", null, NOT_EMPTY);
    ConfigurationKey<String> CONFIG_SOO_SERVER_URL_PREFIX = new ConfigurationKey<>("ssoServerUrlPrefix", null, NOT_EMPTY);

    ConfigurationKey<String> CONFIG_CLIENT_LOGOUT_URI = new ConfigurationKey<>("clientSideLogoutUri", "/logout", NOT_EMPTY);
    ConfigurationKey<Boolean> CONFIG_USE_SESSION = new ConfigurationKey<>("useSession", true, null);
    ConfigurationKey<String> CONFIG_SESSION_KEY = new ConfigurationKey<>("sessionKey", "x-current-session", NOT_EMPTY);
    ConfigurationKey<String> CONFIG_TOKEN_VALIDATION_URI = new ConfigurationKey<>("tokenValidationUri", SSO_SERVER_TOKEN_VALIDATE_URI, NOT_EMPTY);
    ConfigurationKey<String> CONFIG_TOKEN_DESTROY_URI = new ConfigurationKey<>("tokenDestroyUri", SSO_SERVER_TOKEN_DESTROY_URI, NOT_EMPTY);
    ConfigurationKey<String> CONFIG_TOKEN = new ConfigurationKey<>("tokenName", "x-yassos-token", null);
    ConfigurationKey<Boolean> CONFIG_ENCODE_URL = new ConfigurationKey<>("encodeUrl", false, null);
    ConfigurationKey<Boolean> CONFIG_THROW_EXCEPTION_IF_VALIDATE_EXCEPTION = new ConfigurationKey<>("throwExceptionIfTokenValidateException", false, null);
    ConfigurationKey<String> CONFIG_IGNORE_URI_PATTERNS = new ConfigurationKey<>("ignoreUriPatterns", null, null);
}
