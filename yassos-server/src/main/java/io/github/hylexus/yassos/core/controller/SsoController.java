package io.github.hylexus.yassos.core.controller;

import io.github.hylexus.yassos.client.utils.CommonUtils;
import io.github.hylexus.yassos.core.model.LoginForm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static io.github.hylexus.yassos.client.utils.ConfigurationKeys.CALLBACK_ADDRESS_NAME;

/**
 * @author hylexus
 * Created At 2019-06-07 16:40
 */
@Controller
@Slf4j
public class SsoController {

    @GetMapping("/login")
    public ModelAndView login(@RequestParam(required = false, defaultValue = DEFAULT_CALLBACK_URI, name = CALLBACK_ADDRESS_NAME) String callbackUrl) {
        log.info("{}", callbackUrl);
        ModelAndView mv = new ModelAndView("login");
        mv.addObject(CALLBACK_ADDRESS_NAME, callbackUrl);
        return mv;

    }

    @GetMapping("/login-success")
    public String afterLogin() {
        return "login-success";
    }

    private static final String DEFAULT_CALLBACK_URI = "/login-success";

    @RequestMapping("/sign-on")
    public void doLogin(
            LoginForm loginForm,
            @RequestParam(required = false, defaultValue = DEFAULT_CALLBACK_URI, name = CALLBACK_ADDRESS_NAME) String callbackUrl,
            HttpServletResponse response) throws IOException {

        log.info("{}", loginForm);
        String token = "123";

        final String originalUrl;
        if (callbackUrl.equalsIgnoreCase(DEFAULT_CALLBACK_URI)) {
            originalUrl = CommonUtils.generateCallbackUrl(DEFAULT_CALLBACK_URI, token);
        } else {
            originalUrl = CommonUtils.generateCallbackUrl(callbackUrl, token);
        }

        log.info("original url {}", originalUrl);
        response.sendRedirect(originalUrl);
    }
}
