package io.github.hylexus.yassos.client.utils;

import static io.github.hylexus.yassos.client.utils.ConfigurationKeys.CALLBACK_ADDRESS_NAME;
import static io.github.hylexus.yassos.client.utils.ConfigurationKeys.CONFIG_TOKEN;

/**
 * @author hylexus
 * Created At 2019-06-07 19:50
 */
public class CommonUtils {
    public static String generateRedirectToLoginUrl(String loginUrl, String originalUrl) {
        return loginUrl + "?" + CALLBACK_ADDRESS_NAME +
                "=" + originalUrl;
    }

    public static String generateCallbackUrl(String target, String token) {
        if (target.contains("?")) {
            return target + "&" + CONFIG_TOKEN.getDefaultValue() + "=" + token;
        }

        return target + "?" + CONFIG_TOKEN.getDefaultValue() + "=" + token;
    }
}
