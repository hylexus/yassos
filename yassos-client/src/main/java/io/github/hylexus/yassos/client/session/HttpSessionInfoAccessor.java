package io.github.hylexus.yassos.client.session;

import com.alibaba.fastjson.JSON;
import io.github.hylexus.yassos.client.exception.TokenValidateException;
import io.github.hylexus.yassos.core.session.YassosSession;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.util.Objects;

import static io.github.hylexus.yassos.core.session.YassosSession.INVALID_SESSION;

/**
 * @author hylexus
 * Created At 2019-06-11 21:35
 */
@Slf4j
public class HttpSessionInfoAccessor implements SessionInfoAccessor {

    private final OkHttpClient client = new OkHttpClient();

    @Override
    public YassosSession fetchSessionInfo(String token, String url) {
        final String resp = this.doGet(url);
        final YassosSession sessionInfo;
        try {
            sessionInfo = JSON.parseObject(resp, DefaultSessionImpl.class);
        } catch (Exception e) {
            log.error("token validation parse error, token :{}, body : {}", token, resp);
            return INVALID_SESSION;
        }
        log.debug("token validation response : {}", sessionInfo);
        return sessionInfo;
    }

    @Override
    public boolean destroyToken(String token, String url) throws TokenValidateException {
        final String resp = this.doGet(url);
        return Objects.equals("true", resp);
    }

    private static final String EMPTY_RESP = "";

    private String doGet(String url) {
        final Request request = new Request.Builder()
                .url(url)
                .get()
                .build();

        final Response response;
        try {
            response = this.client.newCall(request).execute();
        } catch (IOException e) {
            throw new TokenValidateException(e);
        }

        if (response.body() == null) {
            log.error("yassos server response is empty. url : {}", url);
            return EMPTY_RESP;
        }

        try {
            return response.body().string();
        } catch (IOException e) {
            log.error("token validation request error", e);
            throw new TokenValidateException("an error occurred while read response from yassos server", e);
        }

    }

}
