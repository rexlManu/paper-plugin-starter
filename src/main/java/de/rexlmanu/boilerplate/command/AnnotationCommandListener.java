package de.rexlmanu.boilerplate.command;

import com.google.inject.Inject;
import com.google.inject.TypeLiteral;
import com.google.inject.spi.InjectionListener;
import com.google.inject.spi.TypeEncounter;
import com.google.inject.spi.TypeListener;
import java.util.Arrays;
import java.util.Collections;
import java.util.Set;
import java.util.WeakHashMap;
import lombok.extern.slf4j.Slf4j;
import org.bukkit.command.CommandSender;
import org.incendo.cloud.annotations.AnnotationParser;
import org.incendo.cloud.annotations.Command;

@Slf4j
public class AnnotationCommandListener implements TypeListener {
  private final Set<Object> discoveredComponents = Collections.newSetFromMap(new WeakHashMap<>());
  @Inject private AnnotationParser<CommandSender> annotationParser;

  @Override
  public <I> void hear(TypeLiteral<I> type, TypeEncounter<I> encounter) {
    var rawType = type.getRawType();

    if (Arrays.stream(rawType.getMethods())
        .noneMatch(method -> method.isAnnotationPresent(Command.class))) {
      return;
    }
    encounter.register(
        (InjectionListener<I>)
            instance -> {
              // check if instance is already discovered
              if (!this.discoveredComponents.add(instance)) {
                return;
              }

              try {
                this.annotationParser.parse(instance);
              } catch (Exception e) {
                log.error("Failed to parse command annotations for " + instance.getClass(), e);
              }
            });
  }
}
