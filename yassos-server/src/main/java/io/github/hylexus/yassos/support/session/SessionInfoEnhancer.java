package io.github.hylexus.yassos.support.session;

import io.github.hylexus.yassos.client.model.YassosSession;
import io.github.hylexus.yassos.support.model.UserDetails;

/**
 * @author hylexus
 * Created At 2019-07-04 21:44
 */
public interface SessionInfoEnhancer {

    YassosSession enhance(YassosSession yassosSession, UserDetails userDetails);

    class NoneEnhancementEnhancer implements SessionInfoEnhancer {
        @Override
        public YassosSession enhance(YassosSession yassosSession, UserDetails userDetails) {
            return yassosSession;
        }
    }
}
