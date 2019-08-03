package io.github.hylexus.yassos.config;

import io.github.hylexus.yassos.support.auth.CredentialsMatcher;
import io.github.hylexus.yassos.support.session.enhance.YassosSessionAttrConverter;
import io.github.hylexus.yassos.support.token.TokenGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.boot.ansi.AnsiOutput;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static io.github.hylexus.yassos.support.YassosConfigureConstants.BUILTIN_COMPONENT_ORDER;
import static io.github.hylexus.yassos.support.YassosConfigureConstants.DEPRECATED_COMPONENT_COLOR;

/**
 * @author hylexus
 * Created At 2019-07-03 22:00
 */
@Slf4j
@Configuration
@AutoConfigureOrder(BUILTIN_COMPONENT_ORDER)
public class BuiltinYassosServerAutoConfigure implements ApplicationContextAware {

    protected ApplicationContext applicationContext;

//    @Bean
//    @ConditionalOnProperty(prefix = "yassos.session", name = "type", havingValue = "memory")
//    public SessionManager simpleMemorySessionManager() {
//        log.warn(AnsiOutput.toString(DEPRECATED_COMPONENT_COLOR, "<<Using default SimpleMemorySessionManager, please consider to provide your own implementation of SessionManager>>"));
//        return new SimpleMemorySessionManager();
//    }
//
//    @Bean
//    @ConditionalOnProperty(prefix = "yassos.session", name = "type", havingValue = "redis")
//    public SessionManager simpleRedisSessionManager() {
//        return new SimpleRedisSessionManager();
//    }

    @Bean
    @ConditionalOnMissingBean(TokenGenerator.class)
    public TokenGenerator tokenGenerator() {
        return new TokenGenerator.SimpleUUIDTokenGenerator();
    }

//    @Bean
//    @ConditionalOnMissingBean(UserStore.class)
//    public UserStore builtinUserServiceForDebugging() {
//        log.warn(AnsiOutput.toString(DEPRECATED_COMPONENT_COLOR, ("<<Using default BuiltinUserServiceForDebugging, please consider to provide your own implementation of UserDetailService>>")));
//        return new BuiltinUserServiceForDebugging();
//    }

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
    public YassosServerConfigStatistics yassosServerConfigStatistics() {
        return new YassosServerConfigStatistics();
    }

}
