package de.rexlmanu.boilerplate.event.impl;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import de.rexlmanu.boilerplate.event.EventBus;
import de.rexlmanu.boilerplate.event.EventListenerBuilder;
import java.util.function.Consumer;
import java.util.function.Predicate;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

@Singleton
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class BukkitEventBus implements EventBus {
  private final JavaPlugin plugin;

  @Override
  public <T extends Event> Listener subscribe(
      Class<T> eventType, Consumer<T> action, EventPriority priority, boolean ignoreCancelled) {
    Listener eventListener = new Listener() {};
    Bukkit.getPluginManager()
        .registerEvent(
            eventType,
            eventListener,
            priority,
            (listener, event) -> {
              if (eventType.isInstance(event)) {
                action.accept(eventType.cast(event));
              }
            },
            this.plugin,
            ignoreCancelled);
    return eventListener;
  }

  @Override
  public <T extends Event> EventListenerBuilder<T> on(Class<T> eventType) {
    return new EventListenerBuilderImpl<>(eventType, this.plugin);
  }

  @Override
  public void unsubscribe(Listener listener) {
    HandlerList.unregisterAll(listener);
  }

  @RequiredArgsConstructor
  static class EventListenerBuilderImpl<T extends Event> implements EventListenerBuilder<T> {
    private EventPriority priority = EventPriority.NORMAL;
    private boolean ignoreCancelled = false;
    private Consumer<T> action;
    private Predicate<T> predicate = null;

    private final Class<T> eventType;
    private final JavaPlugin plugin;

    @Override
    public EventListenerBuilder<T> priority(EventPriority priority) {
      this.priority = priority;
      return this;
    }

    @Override
    public EventListenerBuilder<T> ignoreCancelled(boolean ignoreCancelled) {
      this.ignoreCancelled = ignoreCancelled;
      return this;
    }

    @Override
    public EventListenerBuilder<T> filter(Predicate<T> predicate) {
      if (this.predicate == null) {
        this.predicate = predicate;
      } else {
        this.predicate = this.predicate.and(predicate);
      }
      return this;
    }

    @Override
    public Listener subscribe(Consumer<T> action) {
      this.action = action;
      Listener eventListener = new Listener() {};
      Bukkit.getPluginManager()
          .registerEvent(
              this.eventType,
              eventListener,
              this.priority,
              (listener, event) -> {
                if (this.eventType.isInstance(event)) {
                  T castedEvent = this.eventType.cast(event);
                  if (this.predicate == null || this.predicate.test(castedEvent)) {
                    this.action.accept(castedEvent);
                  }
                }
              },
              this.plugin,
              this.ignoreCancelled);
      return eventListener;
    }
  }
}
