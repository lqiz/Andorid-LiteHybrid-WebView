package com.luoruiyi.litehybird;
/**
 * @author luoruiyi
 */

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/** Invoke response function from h5 via string comparison. */
@Documented
@Target(METHOD)
@Retention(RUNTIME)
public @interface INVOKE {
    /**
     * A relative or absolute path, or full URL of the endpoint. This value is optional if the first
     * parameter of the method is annotated with {@link Url @Url}.
     */
    String value() default "";
}
