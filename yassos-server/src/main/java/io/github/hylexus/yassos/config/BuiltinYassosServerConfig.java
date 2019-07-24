package io.github.hylexus.yassos.config;

import io.github.hylexus.yassos.support.auth.CredentialsMatcher;
import io.github.hylexus.yassos.support.auth.UserDetailService;
import io.github.hylexus.yassos.support.auth.user.BuiltinUserServiceForDebugging;
import io.github.hylexus.yassos.support.session.SessionManager;
import io.github.hylexus.yassos.support.session.enhance.YassosSessionAttrConverter;
import io.github.hylexus.yassos.support.session.manager.SimpleMemorySessionManager;
import io.github.hylexus.yassos.support.session.manager.SimpleRedisSessionManager;
import io.github.hylexus.yassos.support.token.TokenGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.ansi.AnsiColor;
import org.springframework.boot.ansi.AnsiOutput;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;

import java.util.function.Function;

/**
 * @author hylexus
 * Created At 2019-07-03 22:00
 */
@Slf4j
public class BuiltinYassosServerConfig implements ApplicationContextAware {

    protected ApplicationContext applicationContext;

    @Bean
    @ConditionalOnProperty(prefix = "yassos.session", name = "type", havingValue = "memory")
    public SessionManager simpleMemorySessionManager() {
        log.warn("<<Using default SimpleMemorySessionManager, please consider to provide your own implementation of SessionManager>>");
        return new SimpleMemorySessionManager();
    }

    @Bean
    @ConditionalOnProperty(prefix = "yassos.session", name = "type", havingValue = "redis")
    public SessionManager simpleRedisSessionManager() {
        return new SimpleRedisSessionManager();
    }

    @Bean
    public TokenGenerator tokenGenerator() {
        return new TokenGenerator.SimpleUUIDTokenGenerator();
    }

//    @Bean
//    @ConditionalOnMissingBean(UserDetailService.class)
//    public UserDetailService builtinUserServiceForDebugging() {
//        log.warn(line("<<Using BuiltinUserServiceForDebugging, please consider to provide your own implementation of UserDetailService>>"));
//        return new BuiltinUserServiceForDebugging();
//    }

    @Bean
    @ConditionalOnMissingBean(CredentialsMatcher.class)
    public CredentialsMatcher credentialsMatcher() {
        log.warn(line("<<Using default PlainTextCredentialsMatcher, please consider to provide your own implementation of CredentialsMatcher>>"));
        return new CredentialsMatcher.PlainTextCredentialsMatcher();
    }

    @Bean
    public YassosSessionAttrConverter yassosSessionAttrConverter() {
        return new YassosSessionAttrConverter.SimpleYassosSessionAttrConverter();
    }


    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Bean
    public CommandLineRunner commandLineRunner() {

        return args -> {
            final AnsiColor builtinComponent = AnsiColor.BRIGHT_CYAN;
            final AnsiColor customComponent = AnsiColor.GREEN;
            final AnsiColor deprecatedComponent = AnsiColor.RED;
            final AnsiColor serverBanner = AnsiColor.BRIGHT_BLUE;

            final String endOfLine = "\n";
//            AnsiOutput.setEnabled(AnsiOutput.Enabled.ALWAYS);
            final StringBuilder sb = new StringBuilder()
                    .append("\n\n")
                    .append(AnsiOutput.toString(serverBanner, "[ ~_~ YASSOS-SERVER ~_~ ]", AnsiColor.DEFAULT))
                    .append(endOfLine)
                    .append("the following configurable components are activated :")
                    .append(endOfLine)
                    .append(AnsiOutput.toString(builtinComponent, "[Builtin-Component]"))
                    .append(AnsiOutput.toString(customComponent, " [Custom-Component] "))
                    .append(AnsiOutput.toString(deprecatedComponent, "[Deprecated-Component]"))
                    .append(endOfLine);

            sb.append(line(1, TokenGenerator.class, cls -> cls == TokenGenerator.SimpleUUIDTokenGenerator.class ? builtinComponent : customComponent))
                    .append(endOfLine)
                    .append(line(2, SessionManager.class, actualClass -> actualClass == SimpleMemorySessionManager.class ? deprecatedComponent : (actualClass == SimpleRedisSessionManager.class ? builtinComponent : customComponent)))
                    .append(endOfLine)
                    .append(line(3, CredentialsMatcher.class, actualClass -> actualClass == CredentialsMatcher.PlainTextCredentialsMatcher.class ? deprecatedComponent : customComponent))
                    .append(endOfLine)
                    .append(line(4, YassosSessionAttrConverter.class, actualClass -> actualClass == YassosSessionAttrConverter.SimpleYassosSessionAttrConverter.class ? builtinComponent : customComponent))
                    .append(endOfLine)
                    .append(line(5, UserDetailService.class, actualClass -> actualClass == BuiltinUserServiceForDebugging.class ? deprecatedComponent : customComponent))
                    .append(endOfLine)
                    .append(AnsiOutput.toString(serverBanner, "[ ~_~ YASSOS-SERVER ~_~ ]", AnsiColor.DEFAULT))
                    .append(endOfLine);
            log.info(sb.toString());
//            AnsiOutput.setEnabled(AnsiOutput.Enabled.DETECT);
        };
    }

    private String line(String content) {
        return AnsiOutput.toString(AnsiColor.RED, content, AnsiColor.DEFAULT);
    }

    private String line(int no, Class<?> superClass, Function<Class<?>, AnsiColor> colorSupplier) {
        final Object bean = applicationContext.getBean(superClass);
        final AnsiColor color = colorSupplier.apply(bean.getClass());
        final String number = AnsiOutput.toString(AnsiColor.BRIGHT_BLACK, no);
        final String beanName = AnsiOutput.toString(color, superClass.getSimpleName(), AnsiColor.DEFAULT);
        return String.format("%1$2s. %2$-41s : %3$10s", number, beanName, bean.getClass().getName());
    }
}
