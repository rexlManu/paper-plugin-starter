package de.rexlmanu.paperpluginstarter.internal.lifecycle.task;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface TimedTask {
  long delay() default 0L;

  long period() default 1000L;

  TimeUnit unit() default TimeUnit.MILLISECONDS;
}
