//package io.github.hylexus.yassos.support.session;
//
//import com.google.common.cache.Cache;
//import com.google.common.cache.CacheBuilder;
//import io.github.hylexus.yassos.client.model.YassosSession;
//import io.github.hylexus.yassos.support.props.YassosSessionProps;
//import org.springframework.beans.factory.InitializingBean;
//import org.springframework.beans.factory.annotation.Autowired;
//
//import java.util.concurrent.TimeUnit;
//
///**
// * @author hylexus
// * Created At 2019-06-09 18:46
// */
//public class BuiltinSessionManagerForDebugging implements SessionManager, InitializingBean {
//
//    @Autowired
//    private YassosSessionProps sessionConfig;
//
//    private Cache<String, YassosSession> cache;
//
//    @Override
//    public YassosSession getSessionByToken(String token) {
//        return cache.getIfPresent(token);
//    }
//
//    @Override
//    public void put(String token, YassosSession yassosSession) {
//        cache.put(token, yassosSession);
//    }
//
//    @Override
//    public void removeSessionByToken(String token) {
//        cache.invalidate(token);
//    }
//
//    @Override
//    public void afterPropertiesSet() throws Exception {
//        cache = CacheBuilder.newBuilder()
//                .expireAfterAccess(sessionConfig.getIdleTime().getSeconds(), TimeUnit.SECONDS)
//                .recordStats()
//                .build();
//    }
//}
