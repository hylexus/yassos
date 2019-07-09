package io.github.hylexus.yassos.support.session;

import io.github.hylexus.yassos.core.session.YassosSessionAttr;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * @author hylexus
 * Created At 2019-07-06 16:03
 */
@Getter
@Setter
@ToString
@Accessors(chain = true)
public class SimpleYassosSessionAttr implements YassosSessionAttr {

    private String avatarUrl;

}
