package io.github.hylexus.yassos.entity;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author hylexus
 * Created At 2019-06-09 19:28
 */
@Data
@Accessors(chain = true)
public class UserEntity {
    private String userId;
    private String username;
    private String password;
}
