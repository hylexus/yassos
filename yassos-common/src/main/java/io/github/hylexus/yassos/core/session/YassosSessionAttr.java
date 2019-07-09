package io.github.hylexus.yassos.core.session;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author hylexus
 * Created At 2019-07-06 13:02
 */
public interface YassosSessionAttr extends Serializable {

    String getAvatarUrl();

    @Getter
    @Setter
    @Accessors(chain = true)
    @ToString
    class DefaultYassosSessionAttr implements YassosSessionAttr {
        private String avatarUrl;
    }
}
