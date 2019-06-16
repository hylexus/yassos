package io.github.hylexus.yassos.client.filter;

import com.alibaba.fastjson.JSON;
import io.github.hylexus.yassos.client.model.Resp;
import io.github.hylexus.yassos.client.utils.CommonUtils;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author hylexus
 * Created At 2019-06-16 19:08
 */
public class DefaultYassosSinOnFilter extends AbstractYassosSingOnFilter {

    @Override
    protected void doAfterLogout(HttpServletRequest req, HttpServletResponse resp, FilterChain chain, String token) throws IOException {
        if (CommonUtils.isAjaxRequest(req)) {
            final Resp<Object> respMsg = Resp.success().setMsg("logout successfully");
            resp.getWriter().print(JSON.toJSONString(respMsg));
            resp.getWriter().flush();
            return;
        }
        resp.sendRedirect("/");
    }

}
