package io.github.hylexus.yassos.client.boot.config;

import com.google.common.collect.Sets;
import io.github.hylexus.yassos.client.handler.BuiltinLogoutHandlerForDebugging;
import io.github.hylexus.yassos.client.handler.LogoutHandler;
import io.github.hylexus.yassos.client.redirect.DefaultRedirectStrategy;
import io.github.hylexus.yassos.client.redirect.RedirectStrategy;
import io.github.hylexus.yassos.client.session.HttpSessionInfoAccessor;
import io.github.hylexus.yassos.client.session.SessionInfoAccessor;
import io.github.hylexus.yassos.client.token.resolver.BearerTokenResolver;
import io.github.hylexus.yassos.client.token.resolver.DefaultTokenResolver;
import io.github.hylexus.yassos.client.token.resolver.TokenResolver;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.ansi.AnsiColor;
import org.springframework.boot.ansi.AnsiOutput;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.util.ClassUtils;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author hylexus
 * Created At 2019-07-14 22:01
 */
@Slf4j
public class YassosClientConfigStatistics implements CommandLineRunner, ApplicationContextAware {

    private ApplicationContext context;
    private static final Map<Class<?>, Set<Class<?>>> mapping;

    public static final AnsiColor BUILTIN_COMPONENT_COLOR = AnsiColor.BRIGHT_CYAN;
    public static final AnsiColor CUSTOM_COMPONENT_COLOR = AnsiColor.GREEN;
    public static final AnsiColor DEPRECATED_COMPONENT_COLOR = AnsiColor.RED;
    public static final AnsiColor UNKNOWN_COMPONENT_TYPE_COLOR = AnsiColor.BRIGHT_BLUE;
    public static final AnsiColor CLIENT_BANNER_COLOR = AnsiColor.BRIGHT_BLUE;

    static {
        final Map<Class<?>, Set<Class<?>>> map = new HashMap<>();
        map.put(TokenResolver.class, Sets.newHashSet(DefaultTokenResolver.class, BearerTokenResolver.class));
        map.put(SessionInfoAccessor.class, Sets.newHashSet(HttpSessionInfoAccessor.class));
        map.put(RedirectStrategy.class, Sets.newHashSet(DefaultRedirectStrategy.class));
        map.put(LogoutHandler.class, Sets.newHashSet(BuiltinLogoutHandlerForDebugging.class));

        mapping = Collections.unmodifiableMap(map);
    }

    @Override
    public void run(String... args) throws Exception {
        final String endOfLine = "\n";
        final StringBuilder sb = new StringBuilder()
                .append(endOfLine)
                .append(endOfLine)
                .append(AnsiOutput.toString(CLIENT_BANNER_COLOR, "[ ~_~ yassos-client-auto-configuration ~_~ ]", AnsiColor.DEFAULT))
                .append(endOfLine)
                .append("the following configurable components are activated :")
                .append(endOfLine)
                .append(AnsiOutput.toString(BUILTIN_COMPONENT_COLOR, "[(B)uiltin-Component]"))
                .append(AnsiOutput.toString(CUSTOM_COMPONENT_COLOR, " [(C)ustom-Component] "))
                .append(AnsiOutput.toString(DEPRECATED_COMPONENT_COLOR, "[(D)eprecated-Component]"))
                .append(endOfLine);

        sb.append(line(1, TokenResolver.class))
                .append(endOfLine)
                .append(line(2, SessionInfoAccessor.class))
                .append(endOfLine)
                .append(line(3, RedirectStrategy.class))
                .append(endOfLine)
                .append(line(4, LogoutHandler.class))
                .append(endOfLine)
                .append(AnsiOutput.toString(CLIENT_BANNER_COLOR, "[ ~_~ yassos-client-auto-configuration ~_~ ]", AnsiColor.DEFAULT))
                .append(endOfLine);

        log.info(sb.toString());
    }

    private String line(int no, Class<?> superClass) {
        final Object bean = context.getBean(superClass);
        final AnsiColor color = detectClassType(superClass, bean.getClass());
        final String number = AnsiOutput.toString(AnsiColor.BRIGHT_BLACK, no);
        final String beanName = AnsiOutput.toString(color, getShortName(color), superClass.getSimpleName(), AnsiColor.DEFAULT);
        return String.format("%1$2s. %2$-45s : %3$10s", number, beanName, bean.getClass().getName());
    }

    private String getShortName(AnsiColor color) {
        if (color == BUILTIN_COMPONENT_COLOR) {
            return "(B) ";
        }
        if (color == CUSTOM_COMPONENT_COLOR) {
            return "(C) ";
        }
        if (color == DEPRECATED_COMPONENT_COLOR) {
            return "(D) ";
        }

        return "";
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.context = applicationContext;
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
        return cls.isAnnotationPresent(Deprecated.class);
    }
}
