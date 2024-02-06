package de.rexlmanu.paperpluginstarter.config.message;

import de.exlll.configlib.Configuration;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@SuppressWarnings("FieldMayBeFinal")
@Configuration
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MessageConfig {
  String pluginReloaded = "<rainbow>Plugin reloaded.</rainbow>";
  String itemReceived = "<rainbow>Item received.</rainbow>";
}
