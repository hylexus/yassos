package cn.yassos.sample.boot.controller;

import io.github.hylexus.yassos.core.config.ConfigurationKeys;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

/**
 * @author hylexus
 * Created At 2019-06-07 20:29
 */
@RestController
public class UserController {

    private static final String DEFAULT_DATETIME_PATTERN = "yyyy-MM-dd HH:mm:ss";

    @GetMapping("/user/me")
    public Object userInfo(HttpSession session) {
        Object attribute = session.getAttribute(ConfigurationKeys.CONFIG_SESSION_KEY.getDefaultValue());
        if (attribute != null) {
            return attribute;
        }

        return "status:OK";
    }
}
