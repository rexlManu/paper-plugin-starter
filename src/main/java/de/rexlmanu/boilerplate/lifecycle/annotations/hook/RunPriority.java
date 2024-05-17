package de.rexlmanu.boilerplate.lifecycle.annotations.hook;

import de.rexlmanu.boilerplate.lifecycle.LifecyclePriority;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface RunPriority {
  LifecyclePriority value();
}
