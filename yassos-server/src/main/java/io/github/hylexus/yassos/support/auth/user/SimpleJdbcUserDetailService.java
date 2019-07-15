package io.github.hylexus.yassos.support.auth.user;

import io.github.hylexus.yassos.support.model.DefaultUserDetails;
import io.github.hylexus.yassos.support.model.UserDetails;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
public class SimpleJdbcUserDetailService implements UserDetailService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadByUsername(String username) {
        String sql = "select id   as userId,\n" +
                "       name as username,\n" +
                "       password,\n" +
                "       locked,\n" +
                "       credential_expired,\n" +
                "       avatarUrl\n" +
                "from yassos_user\n" +
                "where name = ?";
        final List<DefaultUserDetails> list = jdbcTemplate.query(sql, new Object[]{username}, new BeanPropertyRowMapper<>(DefaultUserDetails.class));
        if (CollectionUtils.isEmpty(list)) {
            log.info("can not load user named : {}", username);
            return null;
        }

        DefaultUserDetails user = list.get(0);
        log.debug("UserDetailService load user by JDBC, user:{}", user);
        return user;
    }
}
