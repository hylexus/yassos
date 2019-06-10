package io.github.hylexus.yassos.client.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import static io.github.hylexus.yassos.client.utils.ConfigurationKeys.CALLBACK_ADDRESS_NAME;
import static io.github.hylexus.yassos.client.utils.ConfigurationKeys.CONFIG_TOKEN;

/**
 * @author hylexus
 * Created At 2019-06-07 19:50
 */
public class CommonUtils {
    public static String generateRedirectToLoginUrl(String loginUrl, String originalUrl, Boolean encodeUrl) {
        return loginUrl + "?" + CALLBACK_ADDRESS_NAME +
                "=" + originalUrl;
    }

    public static String generateCallbackUrl(String target, String token) {
        if (target.contains("?")) {
            return target + "&" + CONFIG_TOKEN.getDefaultValue() + "=" + token;
        }

        return target + "?" + CONFIG_TOKEN.getDefaultValue() + "=" + token;
    }


    public static String encodeUrl(final String url) {
        if (url == null) {
            return null;
        }

        try {
            return URLEncoder.encode(url, "UTF-8");
        } catch (final UnsupportedEncodingException e) {
            // never-happened
            return url;
        }
    }
}
