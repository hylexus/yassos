package io.github.hylexus.yassos.support.token;

import io.github.hylexus.yassos.support.model.UsernamePasswordToken;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

/**
 * @author hylexus
 * Created At 2019-06-09 16:36
 */
@FunctionalInterface
public interface TokenGenerator {
    String generateToken(HttpServletRequest request, HttpServletResponse response, UsernamePasswordToken usernamePasswordToken);

    class SimpleUuidTokenGenerator implements TokenGenerator {

        @Override
        public String generateToken(HttpServletRequest request, HttpServletResponse response, UsernamePasswordToken usernamePasswordToken) {
            return UUID.randomUUID().toString().replaceAll("-", "");
        }
    }
}
