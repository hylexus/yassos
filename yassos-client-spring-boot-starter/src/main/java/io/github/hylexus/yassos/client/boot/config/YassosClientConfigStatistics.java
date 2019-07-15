package io.github.hylexus.yassos.client.boot.config;

import io.github.hylexus.yassos.client.handler.BuiltinLogoutHandlerForDebugging;
import io.github.hylexus.yassos.client.handler.LogoutHandler;
import io.github.hylexus.yassos.client.redirect.DefaultRedirectStrategy;
import io.github.hylexus.yassos.client.redirect.RedirectStrategy;
import io.github.hylexus.yassos.client.session.HttpSessionInfoAccessor;
import io.github.hylexus.yassos.client.session.SessionInfoAccessor;
import io.github.hylexus.yassos.client.token.resolver.BearerTokenResolver;
import io.github.hylexus.yassos.client.token.resolver.DefaultTokenResolver;
import io.github.hylexus.yassos.client.token.resolver.TokenResolver;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.ansi.AnsiColor;
import org.springframework.boot.ansi.AnsiOutput;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.function.Function;

/**
 * @author hylexus
 * Created At 2019-07-14 22:01
 */
@Slf4j
public class YassosClientConfigStatistics implements CommandLineRunner, ApplicationContextAware {

    private ApplicationContext context;
    public static final AnsiColor BUILTIN_COMPONENT = AnsiColor.BRIGHT_CYAN;
    public static final AnsiColor CUSTOM_COMPONENT = AnsiColor.GREEN;
    public static final AnsiColor DEPRECATED_COMPONENT = AnsiColor.RED;
    public static final AnsiColor CLIENT_BANNER = AnsiColor.BRIGHT_BLUE;

    @Override
    public void run(String... args) throws Exception {
        final String endOfLine = "\n";
        final StringBuilder sb = new StringBuilder()
                .append(endOfLine)
                .append(endOfLine)
                .append(AnsiOutput.toString(CLIENT_BANNER, "[ <<< yassos-client-auto-configuration >>> ]", AnsiColor.DEFAULT))
                .append(endOfLine)
                .append("the following configurable components are activated :")
                .append(endOfLine)
                .append(AnsiOutput.toString(BUILTIN_COMPONENT, "[Builtin-Component]"))
                .append(AnsiOutput.toString(CUSTOM_COMPONENT, " [Custom-Component] "))
                .append(AnsiOutput.toString(DEPRECATED_COMPONENT, "[Deprecated-Component]"))
                .append(endOfLine);

        sb.append(line(1, TokenResolver.class, actualClass -> actualClass == DefaultTokenResolver.class ? BUILTIN_COMPONENT : (actualClass == BearerTokenResolver.class ? BUILTIN_COMPONENT : CUSTOM_COMPONENT)))
                .append(endOfLine)
                .append(line(2, SessionInfoAccessor.class, actualClass -> actualClass == HttpSessionInfoAccessor.class ? BUILTIN_COMPONENT : CUSTOM_COMPONENT))
                .append(endOfLine)
                .append(line(3, RedirectStrategy.class, actualClass -> actualClass == DefaultRedirectStrategy.class ? BUILTIN_COMPONENT : CUSTOM_COMPONENT))
                .append(endOfLine)
                .append(line(4, LogoutHandler.class, actualClass -> actualClass == BuiltinLogoutHandlerForDebugging.class ? DEPRECATED_COMPONENT : CUSTOM_COMPONENT))
                .append(endOfLine)
                .append(AnsiOutput.toString(CLIENT_BANNER, "[ <<< yassos-client-auto-configuration >>> ]", AnsiColor.DEFAULT))
                .append(endOfLine);

        log.info(sb.toString());
    }

    private String line(int no, Class<?> superClass, Function<Class<?>, AnsiColor> colorSupplier) {
        final Object bean = context.getBean(superClass);
        final AnsiColor color = colorSupplier.apply(bean.getClass());
        final String number = AnsiOutput.toString(AnsiColor.BRIGHT_BLACK, no);
        final String beanName = AnsiOutput.toString(color, superClass.getSimpleName(), AnsiColor.DEFAULT);
        return String.format("%1$2s. %2$-41s : %3$10s", number, beanName, bean.getClass().getName());
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.context = applicationContext;
    }
}
