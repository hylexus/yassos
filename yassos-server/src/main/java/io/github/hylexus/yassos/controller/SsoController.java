package io.github.hylexus.yassos.controller;

import io.github.hylexus.yassos.core.session.YassosSession;
import io.github.hylexus.yassos.core.util.CommonUtils;
import io.github.hylexus.yassos.exception.UserAuthException;
import io.github.hylexus.yassos.service.LocaleMessage;
import io.github.hylexus.yassos.service.UserService;
import io.github.hylexus.yassos.support.model.SimpleYassosSession;
import io.github.hylexus.yassos.support.model.SimpleYassosSessionAttr;
import io.github.hylexus.yassos.support.model.UserDetails;
import io.github.hylexus.yassos.support.model.UsernamePasswordToken;
import io.github.hylexus.yassos.support.props.YassosCookieProps;
import io.github.hylexus.yassos.support.props.YassosGlobalProps;
import io.github.hylexus.yassos.support.session.SessionManager;
import io.github.hylexus.yassos.support.session.enhance.SessionInfoEnhancer;
import io.github.hylexus.yassos.support.token.TokenGenerator;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Optional;

import static io.github.hylexus.yassos.core.config.ConfigurationKeys.*;
import static io.github.hylexus.yassos.support.YassosConfigureConstants.*;

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
    private YassosGlobalProps globalProps;

    @Autowired
    private LocaleMessage localeMessage;

    @GetMapping("/login")
    public ModelAndView login(@RequestParam(required = false, defaultValue = DEFAULT_CALLBACK_URI, name = CALLBACK_ADDRESS_NAME) String callbackUrl) {
        log.info("to login page, redirect_url : {}", callbackUrl);
        ModelAndView mv = new ModelAndView("login");
        mv.addObject(PARAM_KEY_REDIRECT_URL_NAME, CALLBACK_ADDRESS_NAME);
        mv.addObject(PARAM_KEY_REDIRECT_URL_VALUE, callbackUrl);
        // FIXME ...
        mv.addObject("formParam", new UsernamePasswordToken().setUsername("").setPassword(""));
        return mv;
    }

    private static final String DEFAULT_CALLBACK_URI = "/login-success";

    @GetMapping("/login-success")
    public String afterLogin() {
        return "login-success";
    }

    @ExceptionHandler(UserAuthException.class)
    public ModelAndView processUserAuthException(
            UserAuthException e,
            HttpSession session,
            HttpServletRequest request) {
        log.debug("Auth error, username:{}, msg:{}", e.getUsername(), e.getMessage());

        final String i18nCode = e.getI18nCode();
        String errMsg = e.getMessage();
        if (StringUtils.isNoneBlank(i18nCode)) {
            String message = localeMessage.getMessage(i18nCode);
            if (StringUtils.isNotEmpty(message)) {
                errMsg = message;
                session.setAttribute(PARAM_KEY_AUTH_ERR_MSG_KEY_118N, i18nCode);
            }
        }

        session.setAttribute(PARAM_KEY_AUTH_ERR_MSG_KEY, errMsg);

        String redirectUrlName = CommonUtils.getSessionAttr(session, PARAM_KEY_REDIRECT_URL_NAME, CALLBACK_ADDRESS_NAME);
        String redirectUrlValue = CommonUtils.getSessionAttr(session, PARAM_KEY_REDIRECT_URL_VALUE, DEFAULT_CALLBACK_URI);
        return new ModelAndView("redirect:/login?" + redirectUrlName + "=" + redirectUrlValue);
    }

    @RequestMapping("/sign-on")
    public void doLogin(
            UsernamePasswordToken usernamePasswordToken,
            @RequestParam(required = false, defaultValue = DEFAULT_CALLBACK_URI, name = CALLBACK_ADDRESS_NAME) String callbackUrl,
            HttpServletRequest request,
            HttpServletResponse response,
            HttpSession session) throws IOException {

        // FIXME ...
        session.setAttribute("formParam", usernamePasswordToken);
        session.setAttribute(PARAM_KEY_REDIRECT_URL_NAME, CALLBACK_ADDRESS_NAME);
        session.setAttribute(PARAM_KEY_REDIRECT_URL_VALUE, callbackUrl);

        this.paramCheck(usernamePasswordToken);

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

        doAfterLogin(request, response, yassosSession);
        this.clearSession(session);

        response.sendRedirect(originalUrl);
        log.debug("redirect to <{}> after login", originalUrl);
    }

    private void paramCheck(UsernamePasswordToken usernamePasswordToken) {
        final String username = usernamePasswordToken.getUsername();
        if (StringUtils.isEmpty(username))
            throw new UserAuthException("username is null or empty", username);
        if (StringUtils.isEmpty(usernamePasswordToken.getPassword()))
            throw new UserAuthException("password is null or empty", username);
    }

    private void doAfterLogin(HttpServletRequest request, HttpServletResponse response, YassosSession yassosSession) {
        if (!globalProps.getCookie().isEnabled()) {
            log.debug("yassos-cookie was disabled");
            return;
        }
        final Cookie cookie = buildCookie(yassosSession);
        response.addCookie(cookie);
    }

    private void clearSession(HttpSession session) {
        session.removeAttribute(PARAM_KEY_REDIRECT_URL_NAME);
        session.removeAttribute(PARAM_KEY_REDIRECT_URL_VALUE);
        session.removeAttribute(PARAM_KEY_AUTH_ERR_MSG_KEY);
        session.removeAttribute(PARAM_KEY_AUTH_ERR_MSG_KEY_118N);
        session.removeAttribute("formParam");
    }

    private Cookie buildCookie(YassosSession yassosSession) {
        final YassosCookieProps cookieProps = globalProps.getCookie();

        final Cookie cookie = new Cookie(cookieProps.getName(), yassosSession.getToken());
        cookie.setDomain(cookieProps.getDomain());
        cookie.setMaxAge((int) cookieProps.getMaxAge().getSeconds());
        cookie.setPath(cookieProps.getPath());
        cookie.setSecure(cookieProps.isSecure());
        cookie.setHttpOnly(cookieProps.isHttpOnly());
        return cookie;
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
        final LocalDateTime expiredAt = now.plusSeconds((int) globalProps.getSessionManager().getIdleTime().getSeconds());
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
    @GetMapping(SSO_SERVER_TOKEN_DESTROY_URI)
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
    @GetMapping(SSO_SERVER_TOKEN_VALIDATE_URI)
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
