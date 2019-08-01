package io.github.hylexus.yassos.support.utils;

import lombok.Getter;
import org.springframework.boot.ansi.AnsiColor;
import org.springframework.boot.ansi.AnsiOutput;

import static io.github.hylexus.yassos.support.YassosConfigureConstants.COLOR_CONFIG_PARSE_TIPS;
import static io.github.hylexus.yassos.support.YassosConfigureConstants.DEPRECATED_COMPONENT_COLOR;

/**
 * @author hylexus
 * Created At 2019-07-25 23:50
 */
public abstract class AnsiUtils {
    @Getter
    public enum TipsType {
        INFO(COLOR_CONFIG_PARSE_TIPS),
        WARN(DEPRECATED_COMPONENT_COLOR),
        ERROR(DEPRECATED_COMPONENT_COLOR);
        private AnsiColor color;

        TipsType(AnsiColor color) {
            this.color = color;
        }
    }

    public static String configParsingTips(String s) {
        return configParsingTips(TipsType.INFO, s);
    }

    public static String configParsingTips(TipsType tipsType, String content) {
        return AnsiOutput.toString(tipsType.color, content);
    }
}
