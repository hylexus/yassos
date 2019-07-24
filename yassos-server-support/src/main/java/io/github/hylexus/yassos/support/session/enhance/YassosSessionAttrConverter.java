package io.github.hylexus.yassos.support.session.enhance;

import com.alibaba.fastjson.JSON;
import io.github.hylexus.yassos.core.session.YassosSessionAttr;
import io.github.hylexus.yassos.support.model.SimpleYassosSessionAttr;

/**
 * @author hylexus
 * Created At 2019-07-06 17:00
 */
public interface YassosSessionAttrConverter {

    String toString(YassosSessionAttr attr);

    YassosSessionAttr fromString(String string);

    class SimpleYassosSessionAttrConverter implements YassosSessionAttrConverter {
        @Override
        public String toString(YassosSessionAttr attr) {
            return JSON.toJSONString(attr);
        }

        @Override
        public YassosSessionAttr fromString(String string) {
            return JSON.parseObject(string, SimpleYassosSessionAttr.class);
        }
    }
}
