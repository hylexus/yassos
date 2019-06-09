package io.github.hylexus.yassos.service;

import io.github.hylexus.yassos.core.model.LoginForm;

import java.util.UUID;

/**
 * @author hylexus
 * Created At 2019-06-09 16:36
 */
@FunctionalInterface
public interface TokenGenerator {
    String generateToken(LoginForm loginForm);

    class DefaultTokenGenerator implements TokenGenerator {

        @Override
        public String generateToken(LoginForm loginForm) {
            return UUID.randomUUID().toString().replaceAll("-", "");
        }
    }
}
