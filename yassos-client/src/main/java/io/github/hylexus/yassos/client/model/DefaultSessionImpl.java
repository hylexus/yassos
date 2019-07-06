package io.github.hylexus.yassos.client.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * @author hylexus
 * Created At 2019-07-06 16:41
 */
@Getter
@Setter
@Accessors(chain = true)
@ToString
public class DefaultSessionImpl implements YassosSession {
    private String token;
    private String username;

    private Long authenticationDate;
    private Long lastAccessTime;
    private Long expiredAt;

    private YassosSessionAttr.DefaultYassosSessionAttr sessionAttr;
}
