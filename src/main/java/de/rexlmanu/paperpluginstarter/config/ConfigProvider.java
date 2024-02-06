package de.rexlmanu.paperpluginstarter.config;

import com.google.inject.Inject;
import de.rexlmanu.paperpluginstarter.config.message.MessageConfig;
import de.rexlmanu.paperpluginstarter.internal.lifecycle.annotations.DataDirectory;
import de.rexlmanu.paperpluginstarter.internal.lifecycle.annotations.OnPluginEnable;
import de.rexlmanu.paperpluginstarter.internal.lifecycle.annotations.OnPluginReload;
import de.rexlmanu.paperpluginstarter.internal.lifecycle.component.Component;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;

@Component
public class ConfigProvider {
  private final Map<Class<?>, ConfigWrapper<?>> configWrappers = new HashMap<>();
  private final Path dataDirectory;
  private final Logger logger;

  @Inject
  public ConfigProvider(@DataDirectory Path dataDirectory, Logger logger) {
    this.dataDirectory = dataDirectory;
    this.logger = logger;

    this.register(ConfigWrapper.from(MessageConfig.class, "messages.yml"));
    this.register(ConfigWrapper.from(PluginConfig.class, "config.yml"));
  }

  public <C> C get(Class<C> clazz) {
    return clazz.cast(this.configWrappers.get(clazz).config());
  }

  public void register(ConfigWrapper<?> configWrapper) {
    this.configWrappers.put(configWrapper.clazz(), configWrapper);
  }

  @OnPluginEnable
  @OnPluginReload
  public void loadConfigs() {
    this.configWrappers.values().forEach(this::loadConfig);

    this.logger.info("Loaded {} configs.", this.configWrappers.size());
  }

  public <C> void loadConfig(ConfigWrapper<C> configWrapper) {
    C instance = configWrapper.load(this.dataDirectory.resolve(configWrapper.filename()));

    configWrapper.config(instance);
  }
}
