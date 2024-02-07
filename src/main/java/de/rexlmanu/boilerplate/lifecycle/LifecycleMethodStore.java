package de.rexlmanu.boilerplate.lifecycle;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

public record LifecycleMethodStore(
    Map<Class<?>, List<Method>> enableMethods,
    Map<Class<?>, List<Method>> reloadMethods,
    Map<Class<?>, List<Method>> disableMethods,
    Map<Class<?>, List<Method>> scheduledTaskMethods) {}
