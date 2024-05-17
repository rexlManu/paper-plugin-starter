package de.rexlmanu.paperpluginstarter;

import static de.rexlmanu.boilerplate.utils.LifecycleUtils.scan;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Stage;
import de.rexlmanu.boilerplate.PluginModule;
import de.rexlmanu.boilerplate.config.ConfigProvider;
import de.rexlmanu.boilerplate.config.ConfigWrapper;
import de.rexlmanu.boilerplate.lifecycle.LifecycleMethodNotifier;
import de.rexlmanu.boilerplate.lifecycle.LifecycleModule;
import de.rexlmanu.paperpluginstarter.config.MessageConfig;
import de.rexlmanu.paperpluginstarter.config.PluginConfig;
import io.github.classgraph.ScanResult;
import java.util.stream.Stream;
import org.bukkit.plugin.java.JavaPlugin;

public class StarterPlugin extends JavaPlugin {
  private final ScanResult scanResult =
      scan(
          Stream.of(PluginModule.class.getPackageName(), StarterPlugin.class.getPackageName())
              .toArray(String[]::new));
  protected Injector injector;
  @Inject protected ConfigProvider configProvider;

  @Override
  public void onEnable() {
    long start = System.currentTimeMillis();
    try {
      this.injector =
          Guice.createInjector(
              Stage.PRODUCTION, new LifecycleModule(this.scanResult), new PluginModule(this));
      this.injector.injectMembers(this);
      // we need to close the scan result to avoid memory leaks
      this.scanResult.close();
    } catch (Exception e) {
      this.getSLF4JLogger().error("Plugin initialization failed.", e);
      this.disableItself();
      return;
    }

    this.configProvider.register(ConfigWrapper.from(MessageConfig.class, "messages.yml"));
    this.configProvider.register(ConfigWrapper.from(PluginConfig.class, "config.yml"));

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
}
