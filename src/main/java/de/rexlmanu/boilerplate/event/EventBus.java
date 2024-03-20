package de.rexlmanu.boilerplate.event;

import java.util.function.Consumer;
import org.bukkit.event.Event;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

public interface EventBus {
  <T extends Event> Listener subscribe(
      Class<T> eventType, Consumer<T> action, EventPriority priority, boolean ignoreCancelled);

  default <T extends Event> Listener subscribe(
      Class<T> eventType, Consumer<T> action, EventPriority priority) {
    return this.subscribe(eventType, action, priority, false);
  }

  default <T extends Event> Listener subscribe(Class<T> eventType, Consumer<T> action) {
    return this.subscribe(eventType, action, EventPriority.NORMAL);
  }

  <T extends Event> EventListenerBuilder<T> on(Class<T> eventType);

  void unsubscribe(Listener listener);
}
