package io.github.hylexus.yassos.client.utils;

import io.github.hylexus.yassos.client.config.ConfigurationKey;

import static io.github.hylexus.yassos.client.config.ConfigurationKey.ConfigurationKeyValidator.NOT_EMPTY;

/**
 * @author hylexus
 * Created At 2019-06-07 20:14
 */
public interface ConfigurationKeys {
    String CALLBACK_ADDRESS_NAME = "cb";

    ConfigurationKey<String> CONFIG_SSO_LOGIN_URL = new ConfigurationKey<>("ssoLoginUrl", null, null);
    ConfigurationKey<String> CONFIG_TOKEN_VALIDATION_URI = new ConfigurationKey<>("tokenValidationUrl", "validate", NOT_EMPTY);
    ConfigurationKey<String> CONFIG_SERVER_URL_PREFIX = new ConfigurationKey<>("serverUrlPrefix", null, NOT_EMPTY);
    ConfigurationKey<String> CONFIG_TOKEN = new ConfigurationKey<>("tokenName", "x-token", null);
    ConfigurationKey<Boolean> CONFIG_ENCODE_URL = new ConfigurationKey<>("encodeUrl", false, null);
}
