package de.rexlmanu.paperpluginstarter.internal.config;

import java.nio.file.Path;
import java.util.function.Function;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ConfigWrapper<C> {
  private C config;
  private final Path path;
  private final Function<Path, C> loader;

  public void load() {
    this.loader.apply(this.path);
  }
}
