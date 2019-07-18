package io.github.hylexus.yassos.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @author hylexus
 * Created At 2019-07-18 23:34
 */
@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class LanguageSelectOption {
    private String code;
    private String label;
}
