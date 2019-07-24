package io.github.hylexus.yassos.support.model;

import io.github.hylexus.yassos.core.session.YassosSession;
import io.github.hylexus.yassos.core.session.YassosSessionAttr;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;


/**
 * @author hylexus
 * Created At 2019-06-09 16:27
 */
@Setter
@Getter
@Accessors(chain = true)
@ToString
public class SimpleYassosSession implements YassosSession {

    private String token;
    private String username;

    private Long authenticationDate;
    private Long lastAccessTime;
    private Long expiredAt;

    private YassosSessionAttr sessionAttr;
}
