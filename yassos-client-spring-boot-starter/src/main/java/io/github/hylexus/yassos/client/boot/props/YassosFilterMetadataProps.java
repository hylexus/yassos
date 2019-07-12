package io.github.hylexus.yassos.client.boot.props;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Arrays;
import java.util.List;

/**
 * @author hylexus
 * Created At 2019-07-12 22:26
 */
@Getter
@Setter
@ToString
@ConfigurationProperties(prefix = "yassos.filter-metadata")
public class YassosFilterMetadataProps {
    private String name = "yassos-client-filter";
    private Integer order = 1;
    private List<String> urlPatterns = Arrays.asList("/*");
}
