package io.github.hylexus.yassos.client.service.impl;

/**
 * @author hylexus
 * Created At 2019-06-15 19:18
 */

import com.alibaba.fastjson.JSON;
import io.github.hylexus.yassos.client.service.RedirectStrategy;
import io.github.hylexus.yassos.client.utils.CommonUtils;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;

@Slf4j
public class DefaultRedirectStrategy implements RedirectStrategy {
    @Override
    public void redirect(HttpServletRequest request, HttpServletResponse response, String targetUrl) throws IOException {
        if (isAjaxRequest(request)) {
            final HashMap<String, Object> resp = new HashMap<>();
            resp.put("location", targetUrl);
            resp.put("code", HttpServletResponse.SC_UNAUTHORIZED);

            final String responseJson = JSON.toJSONString(resp);

            response.setContentType("application/json;charset=utf-8");
            response.getWriter().write(responseJson);
            response.getWriter().flush();
            log.debug("ajax request, response : {}", responseJson);
            return;
        }
        response.sendRedirect(targetUrl);
    }

    protected boolean isAjaxRequest(HttpServletRequest request) {
        return CommonUtils.isAjaxRequest(request);
    }
}
