package io.github.hylexus.yassos.config;

import io.github.hylexus.oaks.utils.ClassUtils;
import io.github.hylexus.yassos.support.annotation.YassosPlugin;
import io.github.hylexus.yassos.support.auth.CredentialsMatcher;
import io.github.hylexus.yassos.support.token.TokenGenerator;
import io.github.hylexus.yassos.support.user.store.UserStore;
import io.github.hylexus.yassos.support.utils.AnsiUtils;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.util.CollectionUtils;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static io.github.hylexus.yassos.support.YassosConfigureConstants.CONFIG_KEY_YASSOS_HOME;
import static io.github.hylexus.yassos.support.YassosConfigureConstants.CONFIG_KEY_YASSOS_LIB_EXT_DIR;
import static io.github.hylexus.yassos.support.utils.AnsiUtils.configParsingTips;

/**
 * @author hylexus
 * Created At 2019-07-25 22:23
 */
@Slf4j
public class YassosConfigApplicationInitializer implements ApplicationContextInitializer {

    private static final Set<Class<?>> configurableClasses;

    static {
        configurableClasses = Collections.unmodifiableSet(initConfigurableClasses());
    }

    private static HashSet<Class<?>> initConfigurableClasses() {
        final HashSet<Class<?>> classes = new HashSet<>();
        classes.add(TokenGenerator.class);
        classes.add(CredentialsMatcher.class);
        classes.add(UserStore.class);
        return classes;
    }

    private boolean isConfigurableClass(@NonNull Class<?> cls) {
        if (cls == null || cls.isInterface()) {
            return false;
        }

        if (cls.isAnnotationPresent(YassosPlugin.class)) {
            YassosPlugin yassosPlugin = cls.getAnnotation(YassosPlugin.class);
            if (!yassosPlugin.enabled()) {
                log.info(configParsingTips("@YassosPlugin was found on class [{}] but disabled. ==> skip."), cls);
                return false;
            }
        }

        return configurableClasses.stream()
                .anyMatch(configurableClass -> configurableClass.isAssignableFrom(cls));
    }

    private String yassosLibExtDir;

    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {

        if (!init(applicationContext)) {
            return;
        }

        List<Class<?>> classList;
        try {
            ClassLoader classLoader = this.getClassLoader();
            classList = ClassUtils.loadClass(yassosLibExtDir, this::isConfigurableClass, classLoader);
        } catch (IOException e) {
            log.error(configParsingTips(AnsiUtils.TipsType.WARN, "Failed to load plugin"), e);
            throw new RuntimeException(e.getMessage(), e);
        }

        DefaultListableBeanFactory beanFactory = (DefaultListableBeanFactory) applicationContext.getBeanFactory();
        if (CollectionUtils.isEmpty(classList)) {
            log.info(configParsingTips("No class to load.\n"));
            return;
        }
        classList.forEach(cls -> {
            BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(cls);
            beanFactory.registerBeanDefinition(cls.getName(), builder.getRawBeanDefinition());
            log.info(configParsingTips("Class registered as a spring's singleton bean : {}"), cls);
        });
        log.info(configParsingTips("Loading plugin finished\n"));

    }

    private ClassLoader getClassLoader() {
        return Thread.currentThread().getContextClassLoader() == null
                ? ClassLoader.getSystemClassLoader()
                : Thread.currentThread().getContextClassLoader();
    }

    private boolean init(ConfigurableApplicationContext applicationContext) {
        String yassosHome = applicationContext.getEnvironment().getProperty(CONFIG_KEY_YASSOS_HOME);
        if (StringUtils.isEmpty(yassosHome)) {
            log.info(configParsingTips("{} is null or empty, skipping the plugin loading."), CONFIG_KEY_YASSOS_HOME);
            return false;
        }
        yassosLibExtDir = FilenameUtils.normalize(yassosHome + File.separator + CONFIG_KEY_YASSOS_LIB_EXT_DIR, true);
        log.info(configParsingTips("Attempt to load class from lib ext dir : {}"), yassosLibExtDir);
        return true;
    }

}
