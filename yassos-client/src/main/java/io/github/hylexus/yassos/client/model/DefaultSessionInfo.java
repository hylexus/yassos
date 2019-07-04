package io.github.hylexus.yassos.client.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * @author hylexus
 * Created At 2019-06-09 16:27
 */
@Setter
@Getter
@Accessors(chain = true)
@ToString
public class DefaultSessionInfo implements SessionInfo {
    private String sessionId;
    private String username;
    private Date authenticationDate;
    private Date expiredAt;
    private String avatarUrl;

}
