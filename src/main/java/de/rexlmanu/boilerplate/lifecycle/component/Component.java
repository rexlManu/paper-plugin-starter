package de.rexlmanu.boilerplate.lifecycle.component;

import com.google.inject.BindingAnnotation;
import java.lang.annotation.*;

@Target({ElementType.TYPE, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@BindingAnnotation
public @interface Component {}
