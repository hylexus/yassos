package io.github.hylexus.yassos.client.utils;

import io.github.hylexus.yassos.client.config.ConfigurationKey;

/**
 * @author hylexus
 * Created At 2019-06-07 20:14
 */
public interface ConfigurationKeys {
    String CALLBACK_ADDRESS_NAME = "cb";

    ConfigurationKey<String> CONFIG_SSO_LOGIN_URL = new ConfigurationKey<>("ssoLoginUrl", null);
    ConfigurationKey<String> CONFIG_TOKEN = new ConfigurationKey<>("tokenName", "x-token");
}
