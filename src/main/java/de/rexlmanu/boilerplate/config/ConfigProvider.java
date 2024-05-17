package de.rexlmanu.boilerplate.config;

import com.google.inject.Inject;
import de.rexlmanu.boilerplate.lifecycle.annotations.DataDirectory;
import de.rexlmanu.boilerplate.lifecycle.annotations.OnPluginEnable;
import de.rexlmanu.boilerplate.lifecycle.annotations.OnPluginReload;
import de.rexlmanu.boilerplate.lifecycle.component.Component;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import lombok.SneakyThrows;
import org.slf4j.Logger;

@Component
public class ConfigProvider {
  private final Map<Class<?>, ConfigWrapper<?>> configWrappers = new HashMap<>();
  private final Path dataDirectory;
  private final Logger logger;

  @SneakyThrows
  @Inject
  public ConfigProvider(@DataDirectory Path dataDirectory, Logger logger) {
    this.dataDirectory = dataDirectory;
    this.logger = logger;

    Files.createDirectories(dataDirectory);
  }

  public <C> C get(Class<C> clazz) {
    return clazz.cast(this.configWrappers.get(clazz).config());
  }

  public <C> Optional<C> getOrNull(Class<C> clazz) {
    if (!this.configWrappers.containsKey(clazz)) {
      return Optional.empty();
    }
    return Optional.ofNullable(clazz.cast(this.configWrappers.get(clazz).config()));
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
