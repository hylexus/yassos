package io.github.hylexus.yassos.support.session;

import io.github.hylexus.yassos.client.model.SessionInfo;
import io.github.hylexus.yassos.support.model.UserDetails;

/**
 * @author hylexus
 * Created At 2019-07-04 21:44
 */
public interface SessionInfoEnhancer {

    SessionInfo enhance(SessionInfo sessionInfo, UserDetails userDetails);

    class NoneEnhancementEnhancer implements SessionInfoEnhancer {
        @Override
        public SessionInfo enhance(SessionInfo sessionInfo, UserDetails userDetails) {
            return sessionInfo;
        }
    }
}
