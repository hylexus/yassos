package io.github.hylexus.yassos.controller;

import io.github.hylexus.yassos.client.model.DefaultSessionInfo;
import io.github.hylexus.yassos.client.model.SessionInfo;
import io.github.hylexus.yassos.client.utils.CommonUtils;
import io.github.hylexus.yassos.core.model.UsernamePasswordToken;
import io.github.hylexus.yassos.exception.UserAuthException;
import io.github.hylexus.yassos.service.TokenGenerator;
import io.github.hylexus.yassos.service.UserService;
import io.github.hylexus.yassos.support.model.UserDetails;
import io.github.hylexus.yassos.support.session.SessionInfoEnhancer;
import io.github.hylexus.yassos.support.session.SessionManager;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

import static io.github.hylexus.yassos.client.utils.ConfigurationKeys.CALLBACK_ADDRESS_NAME;

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

    @Autowired(required = false)
    private SessionInfoEnhancer sessionInfoEnhancer;

    @GetMapping("/login")
    public ModelAndView login(@RequestParam(required = false, defaultValue = DEFAULT_CALLBACK_URI, name = CALLBACK_ADDRESS_NAME) String callbackUrl) {
        log.info("to login page, redirect_url : {}", callbackUrl);
        ModelAndView mv = new ModelAndView("login");
        mv.addObject("redirect_url_name", CALLBACK_ADDRESS_NAME);
        mv.addObject("redirect_url_value", callbackUrl);
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
            UsernamePasswordToken usernamePasswordToken,
            @RequestParam(required = false, defaultValue = DEFAULT_CALLBACK_URI, name = CALLBACK_ADDRESS_NAME) String callbackUrl,
            HttpServletRequest request, HttpServletResponse response) throws IOException {

        final String username = usernamePasswordToken.getUsername();
        // TODO pre-check for repeated-login
        log.debug("1. user [{}] attempt to login.", username);
        final UserDetails userDetails = userService.login(usernamePasswordToken);

        final String sessionId = tokenGenerator.generateToken(request, response, usernamePasswordToken);
        log.debug("2. sessionId generated : {}", sessionId);

        SessionInfo sessionInfo = this.buildSessionInfo(sessionId, userDetails);
        log.debug("3. built session : {}", sessionInfo);

        if (this.sessionInfoEnhancer != null) {
            sessionInfo = this.sessionInfoEnhancer.enhance(sessionInfo, userDetails);
            log.debug("4. enhanced session : {}", sessionInfo);
        }

        sessionManager.put(sessionId, sessionInfo);
        log.debug("5. putted to sessionManager");

        final String originalUrl;
        if (callbackUrl.equalsIgnoreCase(DEFAULT_CALLBACK_URI)) {
            originalUrl = CommonUtils.generateCallbackUrl(DEFAULT_CALLBACK_URI, sessionId);
        } else {
            originalUrl = CommonUtils.generateCallbackUrl(callbackUrl, sessionId);
        }

        response.sendRedirect(originalUrl);
        log.debug("6. redirect to <{}> after login", originalUrl);
    }

    private SessionInfo buildSessionInfo(String sessionId, UserDetails user) {

        return new DefaultSessionInfo().setAuthenticationDate(new Date())
                .setUsername(user.getUsername())
                .setAvatarUrl(user.getAvatarUrl())
                .setSessionId(sessionId)
                .setExpiredAt(null);
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
        SessionInfo sessionInfo = sessionManager.getSessionByToken(token);
        log.debug("validate session: {}", sessionInfo);
        return sessionInfo;
    }
}
