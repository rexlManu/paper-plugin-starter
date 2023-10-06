package de.rexlmanu.paperpluginstarter.config;

import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;
import de.rexlmanu.paperpluginstarter.internal.config.ConfigWrapper;
import java.nio.file.Path;

public class ConfigModule extends AbstractModule {

  @Override
  protected void configure() {
    this.bind(new TypeLiteral<ConfigWrapper<MessageConfiguration>>() {})
        .toInstance(
            new ConfigWrapper<MessageConfiguration>(
                Path.of("test"),
                path -> {
                  return new MessageConfiguration();
                }));
  }
}
