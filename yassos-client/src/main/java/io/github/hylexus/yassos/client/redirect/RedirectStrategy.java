package io.github.hylexus.yassos.client.redirect;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author hylexus
 */
@FunctionalInterface
public interface RedirectStrategy {

    /**
     * RedirectStrategy when unauthorized request come in
     *
     * @param request   HttpServletRequest
     * @param response  HttpServletResponse
     * @param targetUrl the target url that this request will be redirected to.
     * @throws IOException the exception to throw if there is some exceptions.
     */
    void redirect(HttpServletRequest request, HttpServletResponse response, String targetUrl) throws IOException;

}