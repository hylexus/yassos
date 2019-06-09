package io.github.hylexus.yassos.core.model;

/**
 * @author hylexus
 * Created At 2019-06-07 16:49
 */
public interface SessionInfo {
    String sessionId();

    String userName();

    default Object extra() {
        return null;
    }
}
