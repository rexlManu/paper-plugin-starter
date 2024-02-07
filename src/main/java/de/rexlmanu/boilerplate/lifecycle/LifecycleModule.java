package de.rexlmanu.boilerplate.lifecycle;

import com.google.inject.AbstractModule;
import com.google.inject.Scopes;
import com.google.inject.multibindings.Multibinder;
import de.rexlmanu.boilerplate.lifecycle.annotations.OnPluginDisable;
import de.rexlmanu.boilerplate.lifecycle.annotations.OnPluginEnable;
import de.rexlmanu.boilerplate.lifecycle.annotations.OnPluginReload;
import de.rexlmanu.boilerplate.lifecycle.component.Component;
import de.rexlmanu.boilerplate.lifecycle.task.TimedTask;
import io.github.classgraph.ClassInfo;
import io.github.classgraph.ClassInfoList;
import io.github.classgraph.ScanResult;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.bukkit.event.Listener;

public class LifecycleModule extends AbstractModule {

  private final ScanResult scanResult;
  private final List<Class<?>> componentClasses;
  private final List<Class<Listener>> listenerClasses;
  private final LifecycleMethodStore methodStore;

  public LifecycleModule(ScanResult scanResult) {
    this.scanResult = scanResult;

    var componentClassInfoList =
        this.scanResult
            .getClassesWithAnnotation(Component.class)
            .filter(ClassInfo::isStandardClass);
    this.componentClasses = componentClassInfoList.loadClasses();

    var eventListenerClassInfoList =
        this.scanResult.getClassesImplementing(Listener.class).filter(ClassInfo::isStandardClass);
    this.listenerClasses = eventListenerClassInfoList.loadClasses(Listener.class);

    this.methodStore =
        new LifecycleMethodStore(
            getClassAndMethodsWithAnnotation(
                this.scanResult, OnPluginEnable.class, componentClassInfoList),
            getClassAndMethodsWithAnnotation(
                this.scanResult, OnPluginReload.class, componentClassInfoList),
            getClassAndMethodsWithAnnotation(
                this.scanResult, OnPluginDisable.class, componentClassInfoList),
            getClassAndMethodsWithAnnotation(
                this.scanResult, TimedTask.class, componentClassInfoList));
  }

  @Override
  protected void configure() {
    this.bind(LifecycleMethodStore.class).toInstance(this.methodStore);

    Multibinder<Object> componentBinder =
        Multibinder.newSetBinder(this.binder(), Object.class, Component.class);

    for (var clazz : this.componentClasses) {
      this.bind(clazz).in(Scopes.SINGLETON);

      // Bind all components with @Component key
      componentBinder.addBinding().to(clazz).in(Scopes.SINGLETON);
    }

    var listenerBinder = Multibinder.newSetBinder(this.binder(), Listener.class);
    for (var clazz : this.listenerClasses) {
      listenerBinder.addBinding().to(clazz).in(Scopes.SINGLETON);
    }
  }

  private static Map<Class<?>, List<Method>> getClassAndMethodsWithAnnotation(
      ScanResult scanResult,
      Class<? extends Annotation> annotationClass,
      ClassInfoList... intersect) {
    Map<Class<?>, List<Method>> methodsMap = new HashMap<>();
    var classInfoList =
        scanResult.getClassesWithMethodAnnotation(annotationClass).intersect(intersect);
    for (var classInfo : classInfoList) {
      var methodInfoList =
          classInfo.getMethodInfo().filter(methodInfo -> methodInfo.hasAnnotation(annotationClass));
      List<Method> methods = new ArrayList<>();
      for (var methodInfo : methodInfoList) {
        var method = methodInfo.loadClassAndGetMethod();
        methods.add(method);
      }
      methodsMap.put(classInfo.loadClass(), methods);
    }
    return methodsMap;
  }
}
