package io.github.hylexus.yassos.core.util;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Objects;

import static io.github.hylexus.yassos.core.config.ConfigurationKeys.CONFIG_TOKEN;

/**
 * @author hylexus
 * Created At 2019-06-07 19:50
 */
public class CommonUtils {

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


    public static boolean isAjaxRequest(HttpServletRequest request) {
        return Objects.equals(request.getHeader("X-Requested-With"), "XMLHttpRequest");
    }

    public static String getSessionAttr(HttpSession session, String key, String defaultValue) {
        if (session == null)
            return defaultValue;
        Object attribute = session.getAttribute(key);
        if (attribute == null)
            return defaultValue;
        return (String) attribute;
    }
}
