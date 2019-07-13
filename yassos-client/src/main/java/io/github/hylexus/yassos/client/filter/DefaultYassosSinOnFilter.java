package io.github.hylexus.yassos.client.filter;

import com.alibaba.fastjson.JSON;
import io.github.hylexus.yassos.core.model.Resp;
import io.github.hylexus.yassos.core.util.CommonUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author hylexus
 * Created At 2019-06-16 19:08
 */
public class DefaultYassosSinOnFilter extends AbstractYassosSingOnFilter {

    @Override
    protected void preLogout(HttpServletRequest req, HttpServletResponse resp, String token) throws IOException {
        super.preLogout(req, resp, token);
    }

    @Override
    protected void postLogout(HttpServletRequest req, HttpServletResponse resp, String token, Boolean tokenDestroyedSuccessfully) throws IOException {
        if (CommonUtils.isAjaxRequest(req)) {
            final Resp<Object> respMsg = Resp.success().setMsg("logout successfully");
            resp.getWriter().print(JSON.toJSONString(respMsg));
            resp.getWriter().flush();
            return;
        }
        resp.sendRedirect("/");
    }

}
