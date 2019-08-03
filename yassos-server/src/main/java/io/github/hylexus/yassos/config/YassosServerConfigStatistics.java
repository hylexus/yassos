package io.github.hylexus.yassos.config;

import io.github.hylexus.yassos.support.auth.CredentialsMatcher;
import io.github.hylexus.yassos.support.session.SessionManager;
import io.github.hylexus.yassos.support.session.enhance.YassosSessionAttrConverter;
import io.github.hylexus.yassos.support.token.TokenGenerator;
import io.github.hylexus.yassos.support.user.loader.UserDetailsLoader;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.ansi.AnsiColor;
import org.springframework.boot.ansi.AnsiOutput;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import static io.github.hylexus.yassos.support.YassosConfigureConstants.*;
import static io.github.hylexus.yassos.util.YassosClassUtils.detectClassType;

/**
 * @author hylexus
 * Created At 2019-08-03 22:23
 */
@Slf4j
public class YassosServerConfigStatistics implements CommandLineRunner, ApplicationContextAware {

    private ApplicationContext applicationContext;

    @Override
    public void run(String... args) throws Exception {
        final String endOfLine = "\n";
        final StringBuilder sb = new StringBuilder()
                .append("\n\n")
                .append(AnsiOutput.toString(SERVER_BANNER_COLOR, "[ ~_~ YASSOS-SERVER ~_~ ]", AnsiColor.DEFAULT))
                .append(endOfLine)
                .append("the following configurable components are activated :")
                .append(endOfLine)
                .append(AnsiOutput.toString(BUILTIN_COMPONENT_COLOR, "[Builtin-Component]"))
                .append(AnsiOutput.toString(CUSTOM_COMPONENT_COLOR, " [Custom-Component] "))
                .append(AnsiOutput.toString(DEPRECATED_COMPONENT_COLOR, "[Deprecated-Component]"))
                .append(endOfLine);

        sb.append(line(1, TokenGenerator.class))
                .append(endOfLine)
                .append(line(2, SessionManager.class))
                .append(endOfLine)
                .append(line(3, CredentialsMatcher.class))
                .append(endOfLine)
                .append(line(4, YassosSessionAttrConverter.class))
                .append(endOfLine)
                .append(line(5, UserDetailsLoader.class))
                .append(endOfLine)
                .append(AnsiOutput.toString(SERVER_BANNER_COLOR, "[ ~_~ YASSOS-SERVER ~_~ ]", AnsiColor.DEFAULT))
                .append(endOfLine);
        log.info(sb.toString());
    }


    private String line(int no, Class<?> superClass) {
        final Object bean = applicationContext.getBean(superClass);
        final AnsiColor color = detectClassType(superClass, bean.getClass());
        final String number = AnsiOutput.toString(AnsiColor.BRIGHT_BLACK, no);
        final String beanName = AnsiOutput.toString(color, superClass.getSimpleName(), AnsiColor.DEFAULT);
        return String.format("%1$2s. %2$-41s : %3$10s", number, beanName, bean.getClass().getName());
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
