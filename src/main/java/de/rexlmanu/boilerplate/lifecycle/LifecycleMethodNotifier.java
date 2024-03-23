package de.rexlmanu.boilerplate.lifecycle;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import de.rexlmanu.boilerplate.lifecycle.annotations.hook.RunAfter;
import de.rexlmanu.boilerplate.lifecycle.annotations.hook.RunBefore;
import de.rexlmanu.boilerplate.lifecycle.component.Component;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

@Singleton
public class LifecycleMethodNotifier {
  private final Set<Object> components;
  private final LifecycleMethodStore methodStore;
  private final Injector injector;

  @Inject
  public LifecycleMethodNotifier(
      @Component Set<Object> components, LifecycleMethodStore methodStore, Injector injector) {
    this.components = components;
    this.methodStore = methodStore;
    this.injector = injector;
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
    List<CompiledLifecycleMethod> compiledLifecycleMethods = new ArrayList<>();

    for (var entry : methodsMap.entrySet()) {
      var clazz = entry.getKey();
      for (var component : this.components.toArray()) {
        if (component.getClass().equals(clazz)) {
          for (var method : entry.getValue()) {
            Class<?>[] afterDependencies = new Class<?>[0];
            Class<?>[] beforeDependencies = new Class<?>[0];
            if (method.isAnnotationPresent(RunAfter.class)) {
              afterDependencies = method.getAnnotation(RunAfter.class).value();
            }
            if (method.isAnnotationPresent(RunBefore.class)) {
              beforeDependencies = method.getAnnotation(RunBefore.class).value();
            }

            compiledLifecycleMethods.add(
                new CompiledLifecycleMethod(
                    clazz, method, component, afterDependencies, beforeDependencies));
          }
          break;
        }
      }
    }

    sortCompiledLifecycleMethods(compiledLifecycleMethods);

    for (CompiledLifecycleMethod compiledLifecycleMethod : compiledLifecycleMethods) {
      try {
        compiledLifecycleMethod
            .method()
            .invoke(
                compiledLifecycleMethod.component(),
                Arrays.stream(compiledLifecycleMethod.method().getParameterTypes())
                    .map(this.injector::getInstance)
                    .toArray());

      } catch (Exception e) {
        throw new LifecycleMethodInvocationException(
            compiledLifecycleMethod.originClass(), compiledLifecycleMethod.method(), e);
      }
    }
  }

  record CompiledLifecycleMethod(
      Class<?> originClass,
      Method method,
      Object component,
      Class<?>[] afterDependencies,
      Class<?>[] beforeDependencies) {}

  public static void sortCompiledLifecycleMethods(List<CompiledLifecycleMethod> methods) {
    // Create a graph and an in-degree map for topological sorting
    Map<Class<?>, Set<Class<?>>> graph = new HashMap<>();
    Map<Class<?>, Integer> inDegree = new HashMap<>();

    // Initialize the graph and in-degree map
    for (CompiledLifecycleMethod method : methods) {
      graph.putIfAbsent(method.originClass(), new HashSet<>());
      inDegree.putIfAbsent(method.originClass(), 0);

      for (Class<?> dependency : method.afterDependencies()) {
        graph.putIfAbsent(dependency, new HashSet<>());
        graph.get(dependency).add(method.originClass());

        inDegree.put(method.originClass(), inDegree.getOrDefault(method.originClass(), 0) + 1);
      }

      for (Class<?> dependency : method.beforeDependencies()) {
        graph.putIfAbsent(method.originClass(), new HashSet<>());
        graph.get(method.originClass()).add(dependency);

        inDegree.put(dependency, inDegree.getOrDefault(dependency, 0) + 1);
      }
    }

    // Perform topological sort
    Queue<Class<?>> queue = new LinkedList<>();
    for (Map.Entry<Class<?>, Integer> entry : inDegree.entrySet()) {
      if (entry.getValue() == 0) {
        queue.add(entry.getKey());
      }
    }

    List<CompiledLifecycleMethod> sortedMethods = new ArrayList<>();
    while (!queue.isEmpty()) {
      Class<?> current = queue.poll();
      for (CompiledLifecycleMethod method : methods) {
        if (method.originClass().equals(current)) {
          sortedMethods.add(method);
          break;
        }
      }

      for (Class<?> neighbor : graph.get(current)) {
        inDegree.put(neighbor, inDegree.get(neighbor) - 1);
        if (inDegree.get(neighbor) == 0) {
          queue.add(neighbor);
        }
      }
    }

    if (sortedMethods.size() != methods.size()) {
      throw new RuntimeException(
          "A cycle was detected in the dependencies, cannot perform topological sort.");
    }

    methods.clear();
    methods.addAll(sortedMethods);
  }
}
