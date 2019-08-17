package io.github.hylexus.yassos.util;

import com.google.common.collect.Sets;
import io.github.hylexus.yassos.support.auth.CredentialsMatcher;
import io.github.hylexus.yassos.support.session.enhance.YassosSessionAttrConverter;
import io.github.hylexus.yassos.support.token.TokenGenerator;
import lombok.NonNull;
import org.springframework.boot.ansi.AnsiColor;
import org.springframework.util.ClassUtils;

import java.util.Set;

import static io.github.hylexus.yassos.support.YassosConfigureConstants.*;

/**
 * @author hylexus
 * Created At 2019-07-27 23:59
 */
public abstract class YassosClassUtils {

    private static final Set<String> DEPRECATED_BUILTIN_CLASS_NAME_LIST = Sets.newHashSet(
            "io.github.hylexus.yassos.plugin.user.loader.SimpleFileUserDetailsLoader",
            "io.github.hylexus.yassos.plugin.session.manager.SimpleMemorySessionManager",
            CredentialsMatcher.PlainTextCredentialsMatcher.class.getName()
    );

    private static final Set<String> BUILTIN_CLASS_NAME_LIST = Sets.newHashSet(
            "io.github.hylexus.yassos.plugin.user.loader.SimpleJdbcUserDetailsLoader",
            "io.github.hylexus.yassos.plugin.session.manager.SimpleRedisSessionManager",
            TokenGenerator.SimpleUuidTokenGenerator.class.getName(),
            YassosSessionAttrConverter.SimpleYassosSessionAttrConverter.class.getName()
    );


    public static AnsiColor detectClassType(@NonNull Class<?> superClass, @NonNull Class<?> cls) {
        final Class<?> userClass = ClassUtils.getUserClass(cls);

        if (isDeprecatedBuiltinComponent(superClass, userClass)) {
            return DEPRECATED_COMPONENT_COLOR;
        }

        if (isBuiltinComponent(superClass, userClass)) {
            return BUILTIN_COMPONENT_COLOR;
        }

        return CUSTOM_COMPONENT_COLOR;
    }

    private static boolean isDeprecatedBuiltinComponent(Class<?> superClass, Class<?> cls) {
        return DEPRECATED_BUILTIN_CLASS_NAME_LIST.contains(cls.getName());
    }

    private static boolean isBuiltinComponent(Class<?> superClass, Class<?> cls) {
        return BUILTIN_CLASS_NAME_LIST.contains(cls.getName());
    }

}
