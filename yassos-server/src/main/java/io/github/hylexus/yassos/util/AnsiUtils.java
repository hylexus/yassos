package io.github.hylexus.yassos.util;

import org.springframework.boot.ansi.AnsiOutput;

import static io.github.hylexus.yassos.config.YassosServerConstant.COLOR_CONFIG_PARSE_TIPS;

/**
 * @author hylexus
 * Created At 2019-07-25 23:50
 */
public class AnsiUtils {
    public static String configParsingTips(String s) {
        return AnsiOutput.toString(COLOR_CONFIG_PARSE_TIPS, s);
    }
}
