package de.rexlmanu.paperpluginstarter.internal.lifecycle;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import de.rexlmanu.paperpluginstarter.internal.lifecycle.component.Component;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Singleton
public class LifecycleMethodNotifier {
  private final Set<Object> components;
  private final LifecycleMethodStore methodStore;

  @Inject
  public LifecycleMethodNotifier(
      @Component Set<Object> components, LifecycleMethodStore methodStore) {
    this.components = components;
    this.methodStore = methodStore;
  }

  public void notifyPluginEnable() {
    this.invokeLifecycleMethod(this.methodStore.enableMethods());
  }

  public void notifyPluginReload() {
    this.invokeLifecycleMethod(this.methodStore.reloadMethods());
  }

  public void notifyPluginDisable() {
    this.invokeLifecycleMethod(this.methodStore.disableMethods());
  }

  private void invokeLifecycleMethod(Map<Class<?>, List<Method>> methodsMap)
      throws LifecycleMethodInvocationException {
    for (var entry : methodsMap.entrySet()) {
      var clazz = entry.getKey();
      for (var component : this.components.toArray()) {
        if (component.getClass().equals(clazz)) {
          for (var method : entry.getValue()) {
            try {
              method.invoke(component);
            } catch (IllegalAccessException | InvocationTargetException e) {
              throw new LifecycleMethodInvocationException(clazz, method, e);
            }
          }
          break;
        }
      }
    }
  }
}
