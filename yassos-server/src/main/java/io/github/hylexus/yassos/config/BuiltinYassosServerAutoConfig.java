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
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;

import static io.github.hylexus.yassos.config.YassosServerConstant.*;
import static io.github.hylexus.yassos.util.YassosClassUtils.detectClassType;

/**
 * @author hylexus
 * Created At 2019-07-03 22:00
 */
@Slf4j
@Configuration
@AutoConfigureOrder(Ordered.LOWEST_PRECEDENCE - 10)
public class BuiltinYassosServerAutoConfig implements ApplicationContextAware {

    protected ApplicationContext applicationContext;

    @Bean
    @ConditionalOnProperty(prefix = "yassos.session", name = "type", havingValue = "memory")
    public SessionManager simpleMemorySessionManager() {
        log.warn(AnsiOutput.toString(DEPRECATED_COMPONENT_COLOR, "<<Using default SimpleMemorySessionManager, please consider to provide your own implementation of SessionManager>>"));
        return new SimpleMemorySessionManager();
    }

    @Bean
    @ConditionalOnProperty(prefix = "yassos.session", name = "type", havingValue = "redis")
    public SessionManager simpleRedisSessionManager() {
        return new SimpleRedisSessionManager();
    }

    @Bean
    @ConditionalOnMissingBean(TokenGenerator.class)
    public TokenGenerator tokenGenerator() {
        return new TokenGenerator.SimpleUUIDTokenGenerator();
    }

    @Bean
    @ConditionalOnMissingBean(UserDetailService.class)
    public UserDetailService builtinUserServiceForDebugging() {
        log.warn(AnsiOutput.toString(DEPRECATED_COMPONENT_COLOR, ("<<Using default BuiltinUserServiceForDebugging, please consider to provide your own implementation of UserDetailService>>")));
        return new BuiltinUserServiceForDebugging();
    }

    @Bean
    @ConditionalOnMissingBean(CredentialsMatcher.class)
    public CredentialsMatcher credentialsMatcher() {
        log.warn(AnsiOutput.toString(DEPRECATED_COMPONENT_COLOR, "<<Using default PlainTextCredentialsMatcher, please consider to provide your own implementation of CredentialsMatcher>>"));
        return new CredentialsMatcher.PlainTextCredentialsMatcher();
    }

    @Bean
    @ConditionalOnMissingBean(YassosSessionAttrConverter.class)
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
                    .append(line(5, UserDetailService.class))
                    .append(endOfLine)
                    .append(AnsiOutput.toString(SERVER_BANNER_COLOR, "[ ~_~ YASSOS-SERVER ~_~ ]", AnsiColor.DEFAULT))
                    .append(endOfLine);
            log.info(sb.toString());
        };
    }

    private String line(int no, Class<?> superClass) {
        final Object bean = applicationContext.getBean(superClass);
        final AnsiColor color = detectClassType(superClass, bean.getClass());
        final String number = AnsiOutput.toString(AnsiColor.BRIGHT_BLACK, no);
        final String beanName = AnsiOutput.toString(color, superClass.getSimpleName(), AnsiColor.DEFAULT);
        return String.format("%1$2s. %2$-41s : %3$10s", number, beanName, bean.getClass().getName());
    }
}
