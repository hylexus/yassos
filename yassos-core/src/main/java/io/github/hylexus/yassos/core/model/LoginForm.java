package io.github.hylexus.yassos.core.model;

import lombok.Getter;
import lombok.Setter;

/**
 * @author hylexus
 * Created At 2019-06-07 16:51
 */
@Setter
@Getter
public class LoginForm {

    private String userName;

    private String password;

    private boolean isRememberMe;
}
