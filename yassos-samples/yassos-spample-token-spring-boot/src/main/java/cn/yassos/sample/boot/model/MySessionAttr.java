package cn.yassos.sample.boot.model;

import io.github.hylexus.yassos.core.session.YassosSessionAttr;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * @author hylexus
 * Created At 2019-07-06 17:05
 */
@Getter
@Setter
@Accessors(chain = true)
@ToString
public class MySessionAttr implements YassosSessionAttr {
    private Long userId;
    private String avatarUrl;
}
