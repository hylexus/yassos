package io.github.hylexus.yassos.support.annotation;

import java.lang.annotation.*;

/**
 * @author hylexus
 * Created At 2019-07-25 23:24
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface YassosPlugin {
    boolean enabled() default true;
}
