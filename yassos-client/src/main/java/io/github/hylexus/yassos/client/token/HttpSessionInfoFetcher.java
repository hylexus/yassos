package io.github.hylexus.yassos.client.token;

import com.alibaba.fastjson.JSON;
import io.github.hylexus.yassos.client.exception.TokenValidateException;
import io.github.hylexus.yassos.client.model.DefaultSessionInfo;
import io.github.hylexus.yassos.client.model.SessionInfo;
import io.github.hylexus.yassos.client.token.SessionInfoFetcher;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;

import static io.github.hylexus.yassos.client.model.SessionInfo.INVALID_SESSION;

/**
 * @author hylexus
 * Created At 2019-06-11 21:35
 */
@Slf4j
public class HttpSessionInfoFetcher implements SessionInfoFetcher {

    final private OkHttpClient client = new OkHttpClient();

    @Override
    public SessionInfo fetchSessionInfo(String token, String url) {
        final Request request = new Request.Builder()
                .url(url)
                .get()
                .build();

        final Response response;
        try {
            response = this.client.newCall(request).execute();
        } catch (IOException e) {
            log.error("token validation request error", e);
            return INVALID_SESSION;
        }
        if (response.body() == null) {
            log.error("token validation error, response body is empty!!!");
            return INVALID_SESSION;
        }

        final String body;
        try {
            body = response.body().string();
        } catch (IOException e) {
            log.error("token validation request error", e);
            return INVALID_SESSION;
        }
        if (StringUtils.isEmpty(body)) {
            log.debug("token validation response is empty.");
            return INVALID_SESSION;
        }

        final DefaultSessionInfo sessionInfo;
        try {
            sessionInfo = JSON.parseObject(body, DefaultSessionInfo.class);
        } catch (Exception e) {
            log.error("token validation parse error, body : {}", body);
            return INVALID_SESSION;
        }
        log.debug("token validation response : {}", sessionInfo);
        return sessionInfo;
    }

    @Override
    public boolean expireToken(String token) throws TokenValidateException {
        return false;
    }

}
