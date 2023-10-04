package de.rexlmanu.paperpluginstarter.internal.lifecycle.task;

import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;
import com.google.inject.matcher.Matchers;
import com.google.inject.spi.InjectionListener;
import com.google.inject.spi.TypeEncounter;
import com.google.inject.spi.TypeListener;
import io.papermc.paper.threadedregions.scheduler.AsyncScheduler;
import io.papermc.paper.threadedregions.scheduler.ScheduledTask;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.bukkit.plugin.java.JavaPlugin;

@RequiredArgsConstructor
public class TaskModule extends AbstractModule implements TypeListener {
  private final AsyncScheduler scheduler;
  private final JavaPlugin plugin;

  @Override
  protected void configure() {
    super.bindListener(Matchers.any(), this);
  }

  @Override
  public <I> void hear(TypeLiteral<I> type, TypeEncounter<I> encounter) {
    var rawType = type.getRawType();

    List<Method> methods = Arrays.stream(rawType.getMethods())
        .filter(method -> method.isAnnotationPresent(TimedTask.class)).toList();
    if (methods.isEmpty()) {
      return;
    }

    encounter.register(
        (InjectionListener<I>)
            instance -> {
              for (var method : methods) {
                TimedTask annotation = method.getAnnotation(TimedTask.class);

                this.scheduler.runAtFixedRate(this.plugin, scheduledTask -> {
                  try {
                    // check if first parameter is ScheduledTask, if so, pass it
                    if (method.getParameterCount() == 1 && method.getParameterTypes()[0]
                        .equals(ScheduledTask.class)) {
                      method.invoke(instance, scheduledTask);
                      return;
                    }
                    method.invoke(instance);
                  } catch (Exception e) {
                    this.plugin.getSLF4JLogger()
                        .error("Error while executing async task timer.", e);
                  }
                }, annotation.delay(), annotation.period(), annotation.unit());
              }
            });
  }
}
