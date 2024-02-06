package de.rexlmanu.paperpluginstarter.internal.event;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import java.util.function.Consumer;
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
  public void unsubscribe(Listener listener) {
    HandlerList.unregisterAll(listener);
  }
}
