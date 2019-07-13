package io.github.hylexus.yassos.web.filter;

import com.alibaba.fastjson.JSON;
import io.github.hylexus.yassos.client.filter.YassosSingOnFilter;
import io.github.hylexus.yassos.client.handler.LogoutHandler;
import io.github.hylexus.yassos.core.model.Resp;
import io.github.hylexus.yassos.core.util.CommonUtils;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author hylexus
 * Created At 2019-07-13 17:34
 */
@Slf4j
public class MyYassosSignOnFilter extends YassosSingOnFilter {
    public MyYassosSignOnFilter() {
        this.logoutHandler = new LogoutHandler() {
            @Override
            public void preLogout(HttpServletRequest req, HttpServletResponse resp, String token) throws IOException {

            }

            @Override
            public void postLogout(HttpServletRequest req, HttpServletResponse resp, String token, Boolean tokenDestroyedSuccessfully) throws IOException {
                log.debug("postLogout>> token:{}, tokenDestroyedSuccessfully:{}", token, tokenDestroyedSuccessfully);
                if (CommonUtils.isAjaxRequest(req)) {
                    final Resp<Object> respMsg = Resp.success().setMsg(" logout successfully ");
                    resp.getWriter().print(JSON.toJSONString(respMsg));
                    resp.getWriter().flush();
                    return;
                }
                resp.sendRedirect("index.jsp");

                final Cookie cookie = new Cookie("x-yassos-token", "");
                cookie.setMaxAge(0);
                resp.addCookie(cookie);
                log.debug("cookie [x-yassos-token] was deleted");
            }
        };
    }
}
