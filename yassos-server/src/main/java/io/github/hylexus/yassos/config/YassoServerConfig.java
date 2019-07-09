package io.github.hylexus.yassos.config;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import com.google.common.collect.Lists;
import io.github.hylexus.yassos.service.TokenGenerator;
import io.github.hylexus.yassos.support.auth.CredentialsMatcher;
import io.github.hylexus.yassos.support.props.YassosSessionProps;
import io.github.hylexus.yassos.support.session.YassosSessionAttrConverter;
import io.github.hylexus.yassos.support.session.enhance.SessionInfoEnhancer;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import java.util.List;

/**
 * @author hylexus
 * Created At 2019-07-03 22:00
 */
@Setter
@Getter
@Configuration
@EnableConfigurationProperties({YassosSessionProps.class})
public class YassoServerConfig extends WebMvcConfigurationSupport {

    @Bean
    public TokenGenerator tokenGenerator() {
        return new TokenGenerator.SimpleUUIDTokenGenerator();
    }

    @Bean
    @ConditionalOnMissingBean(CredentialsMatcher.class)
    public CredentialsMatcher credentialsMatcher() {
        return new CredentialsMatcher.PlainTextCredentialsMatcher();
    }

    @Bean
    @ConditionalOnMissingBean(SessionInfoEnhancer.class)
    public SessionInfoEnhancer sessionInfoEnhancer() {
        return new SessionInfoEnhancer.NoneEnhancementEnhancer();
    }

    @Bean
    @ConditionalOnMissingBean(YassosSessionAttrConverter.class)
    public YassosSessionAttrConverter yassosSessionAttrConverter() {
        return new YassosSessionAttrConverter.SimpleYassosSessionAttrConverter();
    }

    @Override
    protected void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        super.configureMessageConverters(converters);

        FastJsonHttpMessageConverter fastConverter = new FastJsonHttpMessageConverter();
        FastJsonConfig fastJsonConfig = new FastJsonConfig();
        fastJsonConfig.setSerializerFeatures(SerializerFeature.PrettyFormat, SerializerFeature.WriteNullListAsEmpty);
        fastConverter.setFastJsonConfig(fastJsonConfig);
        fastConverter.setSupportedMediaTypes(Lists.newArrayList(
                MediaType.APPLICATION_JSON_UTF8,
                MediaType.APPLICATION_JSON,
                MediaType.TEXT_PLAIN,
                MediaType.TEXT_HTML
        ));
        converters.add(fastConverter);
    }
}
