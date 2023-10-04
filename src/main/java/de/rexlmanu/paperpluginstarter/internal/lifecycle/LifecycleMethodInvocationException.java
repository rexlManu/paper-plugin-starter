package de.rexlmanu.paperpluginstarter.internal.lifecycle;

import java.lang.reflect.Method;
import lombok.Getter;

@Getter
public class LifecycleMethodInvocationException extends RuntimeException {
  private final Class<?> componentClass;
  private final Method lifecycleMethod;

  LifecycleMethodInvocationException(Class<?> componentClass, Method lifecycleMethod,
                                     Throwable cause) {
    super(String.format("Failed to invoke lifecycle method in %s.%s",
        componentClass.getCanonicalName(), lifecycleMethod.getName()), cause);
    this.componentClass = componentClass;
    this.lifecycleMethod = lifecycleMethod;
  }
}
