package de.rexlmanu.paperpluginstarter;

import com.google.inject.Guice;
import com.google.inject.Injector;
import org.bukkit.plugin.java.JavaPlugin;

public class StarterPlugin extends JavaPlugin {
  private Injector injector;

  @Override
  public void onEnable() {
    this.injector = Guice.createInjector(
        new StarterModule(this)
    );
  }
}
