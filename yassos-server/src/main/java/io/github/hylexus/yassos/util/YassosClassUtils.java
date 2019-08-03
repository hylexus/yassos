package io.github.hylexus.yassos.util;

import com.google.common.collect.Sets;
import io.github.hylexus.yassos.plugin.session.manager.SimpleMemorySessionManager;
import io.github.hylexus.yassos.support.auth.CredentialsMatcher;
import io.github.hylexus.yassos.support.session.enhance.YassosSessionAttrConverter;
import io.github.hylexus.yassos.support.token.TokenGenerator;
import lombok.NonNull;
import org.springframework.boot.ansi.AnsiColor;
import org.springframework.util.ClassUtils;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static io.github.hylexus.yassos.support.YassosConfigureConstants.*;

/**
 * @author hylexus
 * Created At 2019-07-27 23:59
 */
public abstract class YassosClassUtils {

    private static final Map<Class<?>, Set<Class<?>>> mapping;

    static {
        final Map<Class<?>, Set<Class<?>>> map = new HashMap<>();
        map.put(TokenGenerator.class, Sets.newHashSet(TokenGenerator.SimpleUUIDTokenGenerator.class));
        map.put(CredentialsMatcher.class, Sets.newHashSet(CredentialsMatcher.PlainTextCredentialsMatcher.class));
        map.put(YassosSessionAttrConverter.class, Sets.newHashSet(YassosSessionAttrConverter.SimpleYassosSessionAttrConverter.class));

        mapping = Collections.unmodifiableMap(map);
    }

    public static AnsiColor detectClassType(@NonNull Class<?> superClass, @NonNull Class<?> cls) {
        final Class<?> userClass = ClassUtils.getUserClass(cls);

        if (isDeprecatedBuiltinComponent(superClass, userClass)) {
            return DEPRECATED_COMPONENT_COLOR;
        }
        if (isBuiltinComponent(superClass, userClass)) {
            return BUILTIN_COMPONENT_COLOR;
        }

        final Set<Class<?>> classes = mapping.get(superClass);
        if (CollectionUtils.isEmpty(classes)) {
            return UNKNOWN_COMPONENT_TYPE_COLOR;
        }

        for (Class<?> klass : classes) {
            if (klass == userClass) {
                if (isDeprecated(userClass)) {
                    return DEPRECATED_COMPONENT_COLOR;
                }
                return BUILTIN_COMPONENT_COLOR;
            }
        }

        return CUSTOM_COMPONENT_COLOR;
    }

    private static final Set<String> DEPRECATED_BUILTIN_CLASS_NAME_LIST = Sets.newHashSet(
            "io.github.hylexus.yassos.plugin.user.loader.SimpleFileUserDetailsLoader",
            "io.github.hylexus.yassos.plugin.session.manager.SimpleMemorySessionManager"
    );
    private static final Set<String> BUILTIN_CLASS_NAME_LIST = Sets.newHashSet(
            "io.github.hylexus.yassos.plugin.user.loader.SimpleJdbcUserDetailsLoader",
            "io.github.hylexus.yassos.plugin.session.manager.SimpleRedisSessionManager"
    );

    private static boolean isDeprecatedBuiltinComponent(Class<?> superClass, Class<?> cls) {
        return DEPRECATED_BUILTIN_CLASS_NAME_LIST.contains(cls.getName());
    }

    private static boolean isBuiltinComponent(Class<?> superClass, Class<?> cls) {
        return BUILTIN_CLASS_NAME_LIST.contains(cls.getName());
    }

    private static boolean isDeprecated(Class<?> cls) {
        return cls.isAnnotationPresent(Deprecated.class)
                || cls == CredentialsMatcher.PlainTextCredentialsMatcher.class
                || SimpleMemorySessionManager.class == cls;
    }

}
