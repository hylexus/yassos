package io.github.hylexus.yassos.client.util;

import io.github.hylexus.yassos.core.config.ConfigurationKey;
import io.github.hylexus.yassos.core.config.ConfigurationKeys;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.FilterConfig;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static io.github.hylexus.yassos.core.config.ConfigurationKeys.URI_PATTERNS_SEPARATOR;

/**
 * @author hylexus
 * Created At 2019-07-11 21:56
 */
@Slf4j
public class ServletUtils {


    public static void initString(FilterConfig filterConfig, BooleanSupplier supplier, Consumer<String> consumer, ConfigurationKey<String> configurationKey, boolean forceValidate) {
        if (!supplier.getAsBoolean())
            return;
        final String name = configurationKey.getName();
        final String defaultValue = configurationKey.getDefaultValue();
        final String initParameter = filterConfig.getInitParameter(name);
        if (StringUtils.isEmpty(initParameter)) {
            log.info("[{}] is null, use default value <{}>", name, defaultValue);
            consumer.accept(defaultValue);
        } else {
            log.info("parse [{}] from filterConfig, value : <{}>", name, initParameter);
            configurationKey.validate(initParameter, forceValidate);
            consumer.accept(initParameter);
        }

    }

    public static void initBoolean(FilterConfig filterConfig, BooleanSupplier supplier, Consumer<Boolean> consumer, ConfigurationKey<Boolean> configKey) {
        if (!supplier.getAsBoolean())
            return;

        final String name = configKey.getName();
        final Boolean defaultValue = configKey.getDefaultValue();
        final String initParameter = filterConfig.getInitParameter(name);
        if (StringUtils.isEmpty(initParameter)) {
            log.info("[{}] is null, use default value <{}>", name, defaultValue);
            consumer.accept(defaultValue);
        } else {
            try {
                consumer.accept(Boolean.parseBoolean(initParameter));
            } catch (Exception e) {
                consumer.accept(defaultValue);
                log.error("parse [{}] error, use default value <{}>", name, defaultValue);
            }
        }

    }

    public static List<String> initIgnoreUriPattern(FilterConfig filterConfig) {
        final String initParameter = filterConfig.getInitParameter(ConfigurationKeys.CONFIG_IGNORE_URI_PATTERNS.getName());
        if (StringUtils.isEmpty(initParameter)) {
            return new ArrayList<>();
        }

        return Stream.of(initParameter.split(URI_PATTERNS_SEPARATOR))
                .filter(StringUtils::isNotEmpty)
                .distinct()
                .collect(Collectors.toList());
    }
}
