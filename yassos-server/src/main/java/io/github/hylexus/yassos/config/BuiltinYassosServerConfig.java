package io.github.hylexus.yassos.config;

import io.github.hylexus.yassos.service.TokenGenerator;
import io.github.hylexus.yassos.service.UserDetailService;
import io.github.hylexus.yassos.support.auth.CredentialsMatcher;
import io.github.hylexus.yassos.support.props.YassosSessionProps;
import io.github.hylexus.yassos.support.session.YassosSessionAttrConverter;
import io.github.hylexus.yassos.support.session.manager.SessionManager;
import io.github.hylexus.yassos.support.session.manager.SimpleMemorySessionManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.ansi.AnsiColor;
import org.springframework.boot.ansi.AnsiOutput;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;

/**
 * @author hylexus
 * Created At 2019-07-03 22:00
 */
@Slf4j
public class BuiltinYassosServerConfig implements ApplicationContextAware {

    protected ApplicationContext applicationContext;

    @Bean
    public SessionManager sessionManager() {
        log.warn("<<Using default SimpleMemorySessionManager, please consider to provide your own implementation of SessionManager>>");
        return new SimpleMemorySessionManager();
    }

    @Bean
    public TokenGenerator tokenGenerator() {
        return new TokenGenerator.SimpleUUIDTokenGenerator();
    }

    @Bean
    public CredentialsMatcher credentialsMatcher() {
        log.warn("<<Using default PlainTextCredentialsMatcher, please consider to provide your own implementation of CredentialsMatcher>>");
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
            String prefix = AnsiOutput.toString(AnsiColor.GREEN, "[YASSOS-SERVER]", AnsiColor.DEFAULT);
            log.info("{} started successfully, the following configurable components are activated :", prefix);
            log.info("1. TokenGenerator : {}", applicationContext.getBean(TokenGenerator.class).getClass());
            log.info("2. SessionManager : {}", applicationContext.getBean(SessionManager.class).getClass());
            log.info("3. CredentialsMatcher : {}", applicationContext.getBean(CredentialsMatcher.class).getClass());
            log.info("4. YassosSessionAttrConverter : {}", applicationContext.getBean(YassosSessionAttrConverter.class).getClass());
            log.info("5. UserDetailService : {}", applicationContext.getBean(UserDetailService.class).getClass());
            log.info(">>>");
        };
    }
}
