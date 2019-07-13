package cn.yassos.sample.boot.controller;

import io.github.hylexus.yassos.client.boot.props.YassosClientProps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

/**
 * @author hylexus
 * Created At 2019-06-07 20:29
 */
@RestController
public class UserController {

    /**
     * registered by {@link io.github.hylexus.yassos.client.boot.config.YassosClientAutoConfiguration}
     */
    @Autowired
    private YassosClientProps clientProps;

    @GetMapping("/user/me")
    public Object userInfo(HttpSession session) {
        Object attribute = session.getAttribute(clientProps.getSessionKey());
        if (attribute != null) {
            return attribute;
        }

        return "status:OK";
    }
}
