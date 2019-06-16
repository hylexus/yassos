package io.github.hylexus.yassos.controller;

import io.github.hylexus.yassos.client.model.SessionInfo;
import io.github.hylexus.yassos.client.utils.CommonUtils;
import io.github.hylexus.yassos.core.SessionManager;
import io.github.hylexus.yassos.core.UserService;
import io.github.hylexus.yassos.core.exception.UserAuthException;
import io.github.hylexus.yassos.core.model.LoginForm;
import io.github.hylexus.yassos.service.TokenGenerator;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static io.github.hylexus.yassos.client.utils.ConfigurationKeys.CALLBACK_ADDRESS_NAME;
import static io.github.hylexus.yassos.client.utils.ConfigurationKeys.CONFIG_TOKEN;

/**
 * @author hylexus
 * Created At 2019-06-07 16:40
 */
@Controller
@Slf4j
public class SsoController {

    @Autowired
    private TokenGenerator tokenGenerator;

    @Autowired
    private SessionManager sessionManager;

    @Autowired
    private UserService userService;

    @GetMapping("/login")
    public ModelAndView login(@RequestParam(required = false, defaultValue = DEFAULT_CALLBACK_URI, name = CALLBACK_ADDRESS_NAME) String callbackUrl) {
        log.info("{}", callbackUrl);
        ModelAndView mv = new ModelAndView("login");
        mv.addObject("redirect_url_name",CALLBACK_ADDRESS_NAME);
        mv.addObject("redirect_url_value",callbackUrl);
        return mv;
    }

    private static final String DEFAULT_CALLBACK_URI = "/login-success";

    @GetMapping("/login-success")
    public String afterLogin() {
        return "login-success";
    }

    @ExceptionHandler(UserAuthException.class)
    public ModelAndView processUserAuthException(UserAuthException e) {
        log.debug("error: {}", e.getMessage());
        ModelAndView mv = new ModelAndView("redirect:/login");
        mv.addObject("errMsg", e.getMessage());
        return mv;
    }

    @RequestMapping("/sign-on")
    public void doLogin(
            LoginForm loginForm,
            @RequestParam(required = false, defaultValue = DEFAULT_CALLBACK_URI, name = CALLBACK_ADDRESS_NAME) String callbackUrl,
            HttpServletResponse response) throws IOException {

        log.debug("request param : {}", loginForm);
        final String sessionId = tokenGenerator.generateToken(loginForm);
        final SessionInfo sessionInfo = userService.login(loginForm);
        sessionInfo.sessionId(sessionId);
        sessionManager.put(sessionId, sessionInfo);
        log.debug("login in newly, sessionId = {}", sessionId);

        doAfterLoginSuccess(sessionId, loginForm, response);
        final String originalUrl;
        if (callbackUrl.equalsIgnoreCase(DEFAULT_CALLBACK_URI)) {
            originalUrl = CommonUtils.generateCallbackUrl(DEFAULT_CALLBACK_URI, sessionId);
        } else {
            originalUrl = CommonUtils.generateCallbackUrl(callbackUrl, sessionId);
        }

        log.debug("original url {}", originalUrl);
        response.sendRedirect(originalUrl);
    }

    private void doAfterLoginSuccess(final String token, final LoginForm loginForm, final HttpServletResponse response) {
        final Cookie cookie = new Cookie(CONFIG_TOKEN.getDefaultValue(), token);
        cookie.setMaxAge(loginForm.isRememberMe() ? Integer.MAX_VALUE : -1);
        cookie.setPath("/");
        cookie.setDomain("mine.com");
        response.addCookie(cookie);
    }

    @ResponseBody
    @GetMapping("/sign-out")
    public Boolean signOut(
            @RequestParam(required = false, name = "token", defaultValue = "") String token) {
        if (StringUtils.isEmpty(token)) {
            return true;
        }
        sessionManager.removeSessionByToken(token);
        log.debug("logout.... token : {}", token);

        return true;
    }

    @ResponseBody
    @GetMapping("/validate")
    public SessionInfo userInfo(@RequestParam("token") String token) {
        return sessionManager.getSessionByToken(token);
    }
}
