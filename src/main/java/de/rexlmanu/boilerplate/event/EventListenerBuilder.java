package de.rexlmanu.boilerplate.event;

import java.util.function.Consumer;
import java.util.function.Predicate;
import org.bukkit.event.Event;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

public interface EventListenerBuilder<T extends Event> {
  EventListenerBuilder<T> priority(EventPriority priority);

  EventListenerBuilder<T> ignoreCancelled(boolean ignoreCancelled);

  EventListenerBuilder<T> filter(Predicate<T> predicate);

  Listener subscribe(Consumer<T> action);
}
