package io.github.hylexus.yassos.client.config;

import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

import static io.github.hylexus.yassos.client.utils.ConfigurationKeys.CONFIG_TOKEN;

/**
 * @author hylexus
 * Created At 2019-06-07 19:22
 */
@FunctionalInterface
public interface TokenResolver {
    Optional<String> resolveToken(HttpServletRequest request);

    class DefaultTokenResolver implements TokenResolver {

        @Override
        public Optional<String> resolveToken(HttpServletRequest request) {
            String header = request.getHeader(CONFIG_TOKEN.getDefaultValue());

            if (StringUtils.isNotBlank(header))
                return Optional.of(header);

            return Optional.ofNullable(request.getParameter(CONFIG_TOKEN.getDefaultValue()));
        }
    }
}
