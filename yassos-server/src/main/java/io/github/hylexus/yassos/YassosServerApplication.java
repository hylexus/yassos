package io.github.hylexus.yassos;

import io.github.hylexus.yassos.support.props.YassosGlobalProps;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

/**
 * @author hylexus
 * Created At 2019-06-07 16:38
 */
@SpringBootApplication(excludeName = "io.github.hylexus.yassos.plugin")
@EnableConfigurationProperties({YassosGlobalProps.class})
public class YassosServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(YassosServerApplication.class, args);
    }

}
