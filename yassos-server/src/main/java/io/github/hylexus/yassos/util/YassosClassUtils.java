package io.github.hylexus.yassos.util;

import io.github.hylexus.yassos.support.auth.CredentialsMatcher;
import io.github.hylexus.yassos.support.auth.user.BuiltinUserServiceForDebugging;
import io.github.hylexus.yassos.support.session.SessionManager;
import io.github.hylexus.yassos.support.session.enhance.YassosSessionAttrConverter;
import io.github.hylexus.yassos.support.session.manager.SimpleMemorySessionManager;
import io.github.hylexus.yassos.support.session.manager.SimpleRedisSessionManager;
import io.github.hylexus.yassos.support.token.TokenGenerator;
import io.github.hylexus.yassos.support.user.store.UserStore;
import lombok.NonNull;
import org.assertj.core.util.Sets;
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
        map.put(UserStore.class, Sets.newLinkedHashSet(/*SimpleJdbcUserDetailService.class,*/ BuiltinUserServiceForDebugging.class));
        map.put(TokenGenerator.class, Sets.newLinkedHashSet(TokenGenerator.SimpleUUIDTokenGenerator.class));
        map.put(SessionManager.class, Sets.newLinkedHashSet(SimpleMemorySessionManager.class, SimpleRedisSessionManager.class));
        map.put(CredentialsMatcher.class, Sets.newLinkedHashSet(CredentialsMatcher.PlainTextCredentialsMatcher.class));
        map.put(YassosSessionAttrConverter.class, Sets.newLinkedHashSet(YassosSessionAttrConverter.SimpleYassosSessionAttrConverter.class));

        mapping = Collections.unmodifiableMap(map);
    }

    public static AnsiColor detectClassType(@NonNull Class<?> superClass, @NonNull Class<?> cls) {
        final Class<?> userClass = ClassUtils.getUserClass(cls);

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

    private static boolean isDeprecated(Class<?> cls) {
        return cls.isAnnotationPresent(Deprecated.class)
                || cls == CredentialsMatcher.PlainTextCredentialsMatcher.class
                || SimpleMemorySessionManager.class == cls;
    }

}
