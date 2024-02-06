package de.rexlmanu.paperpluginstarter.config;

import de.exlll.configlib.Configuration;
import lombok.Getter;

@SuppressWarnings("FieldMayBeFinal")
@Configuration
@Getter
public class MessageConfig {
  private String pluginReloaded = "<rainbow>Plugin reloaded.</rainbow>";
  private String itemReceived = "<rainbow>Item received.</rainbow>";
}
