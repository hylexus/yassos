package io.github.hylexus.yassos.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author hylexus
 * Created At 2019-06-07 16:51
 */
@Setter
@Getter
@ToString
public class UsernamePasswordToken {

    private String username;

    private String password;

}