package de.rexlmanu.boilerplate.utils;

import io.github.classgraph.ClassGraph;
import io.github.classgraph.ScanResult;

public class LifecycleUtils {
  public static ScanResult scan(String[] packages) {
    return new ClassGraph()
        .acceptPackages(packages)
        .enableAnnotationInfo()
        .enableClassInfo()
        .enableMethodInfo()
        .scan();
  }
}
