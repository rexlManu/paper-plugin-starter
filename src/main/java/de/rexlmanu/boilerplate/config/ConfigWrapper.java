package de.rexlmanu.boilerplate.config;

import de.exlll.configlib.ConfigLib;
import de.exlll.configlib.YamlConfigurations;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import lombok.Getter;
import lombok.Setter;

@Getter
public abstract class ConfigWrapper<C> {
  public static <C> ConfigWrapper<C> from(Class<C> clazz, String filename) {
    return new ConfigWrapper<>(filename, clazz) {
      @Override
      public C load(Path path) {
        return YamlConfigurations.update(
            path,
            clazz,
            ConfigLib.BUKKIT_DEFAULT_PROPERTIES.toBuilder()
                .charset(StandardCharsets.UTF_8)
                .build());
      }
    };
  }

  private final String filename;
  private final Class<C> clazz;
  @Setter private C config;

  protected ConfigWrapper(String filename, Class<C> clazz) {
    this.filename = filename;
    this.clazz = clazz;
  }

  public abstract C load(Path path);
}
