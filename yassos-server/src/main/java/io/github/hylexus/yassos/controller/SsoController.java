package io.github.hylexus.yassos.controller;

import io.github.hylexus.yassos.core.session.YassosSession;
import io.github.hylexus.yassos.core.util.CommonUtils;
import io.github.hylexus.yassos.exception.UserAuthException;
import io.github.hylexus.yassos.model.UsernamePasswordToken;
import io.github.hylexus.yassos.service.TokenGenerator;
import io.github.hylexus.yassos.service.UserService;
import io.github.hylexus.yassos.support.model.UserDetails;
import io.github.hylexus.yassos.support.props.YassosSessionProps;
import io.github.hylexus.yassos.support.session.SimpleYassosSession;
import io.github.hylexus.yassos.support.session.SimpleYassosSessionAttr;
import io.github.hylexus.yassos.support.session.enhance.SessionInfoEnhancer;
import io.github.hylexus.yassos.support.session.manager.SessionManager;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

import static io.github.hylexus.yassos.core.config.ConfigurationKeys.CALLBACK_ADDRESS_NAME;

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

    @Autowired
    private YassosSessionProps sessionProps;

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

        log.debug("user [{}] attempt to login.", username);

        final UserDetails userDetails = userService.login(usernamePasswordToken);

        final YassosSession yassosSession = buildSession(request, response, usernamePasswordToken, userDetails);

        final String originalUrl;
        if (callbackUrl.equalsIgnoreCase(DEFAULT_CALLBACK_URI)) {
            originalUrl = CommonUtils.generateCallbackUrl(DEFAULT_CALLBACK_URI, yassosSession.getToken());
        } else {
            originalUrl = CommonUtils.generateCallbackUrl(callbackUrl, yassosSession.getToken());
        }

        response.sendRedirect(originalUrl);
        log.debug("redirect to <{}> after login", originalUrl);
    }

    private YassosSession buildSession(HttpServletRequest request, HttpServletResponse response, UsernamePasswordToken usernamePasswordToken, UserDetails userDetails) {
        final Optional<YassosSession> existingSession = this.sessionManager.getSessionByUsername(usernamePasswordToken.getUsername(), true);
        if (existingSession.isPresent()) {
            log.debug("return a existing session");
            return existingSession.get();
        }

        final String token = tokenGenerator.generateToken(request, response, usernamePasswordToken);
        log.debug("token generated : {}", token);

        YassosSession session = this.createNewSession(token, userDetails);
        log.debug("built session : {}", session);

        if (this.sessionInfoEnhancer != null) {
            session = this.sessionInfoEnhancer.enhance(session, userDetails);
            log.debug("enhanced session : {}", session);
        }

        sessionManager.put(token, session);
        log.debug("a new session putted to sessionManager");
        return session;
    }

    private YassosSession createNewSession(String sessionId, UserDetails user) {

        final LocalDateTime now = org.joda.time.LocalDateTime.now();
        final LocalDateTime expiredAt = now.plusSeconds((int) sessionProps.getIdleTime().getSeconds());
        final SimpleYassosSessionAttr sessionAttr = new SimpleYassosSessionAttr()
                .setAvatarUrl(user.getAvatarUrl());

        return new SimpleYassosSession().setAuthenticationDate(now.toDate().getTime())
                .setUsername(user.getUsername())
                .setToken(sessionId)
                .setExpiredAt(expiredAt.toDate().getTime())
                .setLastAccessTime(now.toDate().getTime())
                .setSessionAttr(sessionAttr);
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
    public YassosSession userInfo(@RequestParam("token") String token) {
        final Optional<YassosSession> sessionInfo = sessionManager.getSessionByToken(token, true);
        if (sessionInfo.isPresent()) {
            YassosSession session = sessionInfo.get();
            log.debug("[validate-session] token={}, result: {}", token, session);
            return session;
        }
        log.debug("[validate-session] token={}, result: null", token);
        return null;
    }
}
