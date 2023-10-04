package de.rexlmanu.paperpluginstarter.internal.event;

import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;
import com.google.inject.matcher.Matchers;
import com.google.inject.spi.InjectionListener;
import com.google.inject.spi.TypeEncounter;
import com.google.inject.spi.TypeListener;
import lombok.RequiredArgsConstructor;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

@RequiredArgsConstructor
public class EventModule extends AbstractModule implements TypeListener {
  private final JavaPlugin plugin;
  private final PluginManager pluginManager;

  @Override
  protected void configure() {
    super.bindListener(Matchers.any(), this);
  }

  @Override
  public <I> void hear(TypeLiteral<I> type, TypeEncounter<I> encounter) {
    if (Listener.class.isAssignableFrom(type.getRawType())) {
      encounter.register(
          (InjectionListener<I>)
              instance -> {
                if (HandlerList.getRegisteredListeners(this.plugin).stream()
                    .noneMatch(
                        registeredListener -> registeredListener.getListener().equals(instance))) {
                  this.pluginManager.registerEvents((Listener) instance, this.plugin);
                }
              });
    }
  }
}
