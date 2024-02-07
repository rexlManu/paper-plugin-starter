package de.rexlmanu.paperpluginstarter;

import de.rexlmanu.boilerplate.BasePlugin;
import de.rexlmanu.boilerplate.config.ConfigWrapper;
import de.rexlmanu.paperpluginstarter.config.PluginConfig;
import de.rexlmanu.paperpluginstarter.config.message.MessageConfig;

public class StarterPlugin extends BasePlugin {
  public StarterPlugin() {
    super(new String[] {StarterPlugin.class.getPackageName()});
  }

  @Override
  public void onPluginEnable() {
    this.configProvider.register(ConfigWrapper.from(MessageConfig.class, "messages.yml"));
    this.configProvider.register(ConfigWrapper.from(PluginConfig.class, "config.yml"));
  }
}
