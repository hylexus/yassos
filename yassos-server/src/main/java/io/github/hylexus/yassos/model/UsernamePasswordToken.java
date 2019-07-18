package io.github.hylexus.yassos.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * @author hylexus
 * Created At 2019-06-07 16:51
 */
@Setter
@Getter
@ToString
@Accessors(chain = true)
public class UsernamePasswordToken {

    private String username;

    private String password;

}
