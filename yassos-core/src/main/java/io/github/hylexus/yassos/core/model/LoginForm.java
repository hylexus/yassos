package io.github.hylexus.yassos.core.model;

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
public class LoginForm {

    private String username;

    private String password;

    private boolean isRememberMe;
}
