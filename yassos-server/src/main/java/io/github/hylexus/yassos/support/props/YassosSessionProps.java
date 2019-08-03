//package io.github.hylexus.yassos.support.props;
//
//import lombok.Getter;
//import lombok.Setter;
//import org.springframework.boot.context.properties.ConfigurationProperties;
//
//import java.time.Duration;
//
///**
// * @author hylexus
// * Created At 2019-07-03 21:42
// */
//@Setter
//@Getter
//@ConfigurationProperties(prefix = "yassos.session")
//public class YassosSessionProps {
//
//    private SessionStoreType type = SessionStoreType.MEMORY;
//    private Duration idleTime = Duration.ofMinutes(30);
//    private YassosRedisSessionStoreProps redis = new YassosRedisSessionStoreProps();
//
//    public enum SessionStoreType {
//        MEMORY,
//        REDIS
//    }
//}
