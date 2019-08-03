package io.github.hylexus.yassos.plugin.user.loader;

import com.alibaba.fastjson.JSON;
import io.github.hylexus.yassos.support.exception.PluginLoadingException;
import io.github.hylexus.yassos.support.model.DefaultUserDetails;
import io.github.hylexus.yassos.support.model.UserDetails;
import io.github.hylexus.yassos.support.user.loader.UserDetailsLoader;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.CollectionUtils;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.ResourceUtils;
import org.springframework.util.StringUtils;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.github.hylexus.yassos.support.utils.AnsiUtils.TipsType.*;
import static io.github.hylexus.yassos.support.utils.AnsiUtils.configParsingTips;

/**
 * @author hylexus
 * Created At 2019-08-01 23:40
 */
@Slf4j
public class SimpleFileUserDetailsLoader implements UserDetailsLoader, InitializingBean {

    private String fileLocation;
    private Map<String, UserDetails> userMapping = new HashMap<>();

    public SimpleFileUserDetailsLoader(String fileLocation) {
        this.fileLocation = fileLocation;
    }

    @Override
    public UserDetails loadByUsername(String username) {
        UserDetails user = userMapping.get(username);
        if (user == null) {
            log.debug("can not load user named : {}", username);
            return null;
        }

        log.debug("UserDetailService load user from FILE, user:{}", user);
        return user;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        if (StringUtils.isEmpty(fileLocation)) {
            throw new PluginLoadingException("fileLocation is empty");
        }

        try (InputStream inputStream = ResourceUtils.getURL(fileLocation).openStream()) {
            String jsonString = FileCopyUtils.copyToString(new InputStreamReader(inputStream));
            List<DefaultUserDetails> userList = JSON.parseArray(jsonString, DefaultUserDetails.class);
            if (CollectionUtils.isEmpty(userList)) {
                log.warn(configParsingTips(ERROR, "No user found from file : {}"), fileLocation);
                return;
            }

            Map<String, UserDetails> map = new HashMap<>();
            for (DefaultUserDetails user : userList) {
                if (map.containsKey(user.getUsername())) {
                    log.warn(configParsingTips(WARN, "username is duplicate, value : {}"), user.getUsername());
                }
                map.put(user.getUsername(), user);
            }

            this.userMapping = Collections.unmodifiableMap(map);
            log.info(configParsingTips(INFO, ">>| UserStore initialized from file : '{}', size = {}"), fileLocation, map.size());
        }
    }
}
