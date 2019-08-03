package io.github.hylexus.yassos.plugin.user.loader;

import io.github.hylexus.yassos.support.model.DefaultUserDetails;
import io.github.hylexus.yassos.support.model.UserDetails;
import io.github.hylexus.yassos.support.user.loader.UserDetailsLoader;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * @author hylexus
 * Created At 2019-07-15 21:19
 */
@Slf4j
public class SimpleJdbcUserDetailsLoader implements UserDetailsLoader {

    private JdbcTemplate jdbcTemplate;
    private String sqlToLoadUserDetails;

    public SimpleJdbcUserDetailsLoader(JdbcTemplate jdbcTemplate, String sqlToLoadUserDetails) {
        this.jdbcTemplate = jdbcTemplate;
        this.sqlToLoadUserDetails = sqlToLoadUserDetails;
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadByUsername(String username) {
        final List<DefaultUserDetails> list = jdbcTemplate.query(sqlToLoadUserDetails, new Object[]{username}, new BeanPropertyRowMapper<>(DefaultUserDetails.class));
        if (CollectionUtils.isEmpty(list)) {
            log.debug("can not load user named : {}", username);
            return null;
        }

        DefaultUserDetails user = list.get(0);
        log.debug("UserDetailService load user by JDBC, user:{}", user);
        return user;
    }
}
