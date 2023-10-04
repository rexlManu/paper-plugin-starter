package de.rexlmanu.paperpluginstarter.internal;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Stage;
import de.rexlmanu.paperpluginstarter.internal.lifecycle.LifecycleMethodNotifier;
import de.rexlmanu.paperpluginstarter.internal.lifecycle.LifecycleModule;
import io.github.classgraph.ClassGraph;
import java.io.IOException;
import java.nio.file.Files;
import lombok.RequiredArgsConstructor;
import org.bukkit.plugin.java.JavaPlugin;

@RequiredArgsConstructor
public class BasePlugin extends JavaPlugin {
  protected Injector injector;
  protected final String[] scannablePackages;
  private LifecycleModule lifecycleModule;

  @Override
  public void onLoad() {
    long start = System.currentTimeMillis();
    try (var result =
        new ClassGraph()
            .acceptPackages(this.scannablePackages)
            .enableAnnotationInfo()
            .enableClassInfo()
            .enableMethodInfo()
            .scan()) {
      this.lifecycleModule = new LifecycleModule(result);
    }

    this.getSLF4JLogger().info("Plugin loaded in {}ms.", System.currentTimeMillis() - start);
  }

  @Override
  public void onEnable() {
    long start = System.currentTimeMillis();
    try {
      Files.createDirectories(this.getDataFolder().toPath());
    } catch (IOException e) {
      this.getSLF4JLogger().error("Can't create data folder.", e);
      this.disableItself();
      return;
    }

    try {
      this.injector =
          Guice.createInjector(Stage.PRODUCTION, this.lifecycleModule, new PluginModule(this));
    } catch (Exception e) {
      this.getSLF4JLogger().error("Plugin initialization failed.", e);
      this.disableItself();
      return;
    }

    this.injector.getInstance(LifecycleMethodNotifier.class).notifyPluginEnable();

    this.getSLF4JLogger().info("Plugin enabled in {}ms.", System.currentTimeMillis() - start);
  }

  @Override
  public void onDisable() {
    // Only happens if the plugin initialization failed.
    if (this.injector == null) {
      return;
    }
    long start = System.currentTimeMillis();
    this.injector.getInstance(LifecycleMethodNotifier.class).notifyPluginDisable();

    this.getSLF4JLogger().info("Plugin disabled in {}ms.", System.currentTimeMillis() - start);
  }

  public void onReload() {
    long start = System.currentTimeMillis();
    this.injector.getInstance(LifecycleMethodNotifier.class).notifyPluginReload();
    this.getSLF4JLogger().info("Plugin reloaded in {}ms.", System.currentTimeMillis() - start);
  }

  public void disableItself() {
    this.setEnabled(false);
  }

  public void saveResource(String resourcePath) {
    if (!Files.exists(this.getDataFolder().toPath().resolve(resourcePath))) {
      this.saveResource(resourcePath, false);
    }
  }
}
