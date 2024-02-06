package de.rexlmanu.paperpluginstarter.command;

import cloud.commandframework.ArgumentDescription;
import cloud.commandframework.CommandManager;
import com.google.inject.Inject;
import de.rexlmanu.paperpluginstarter.config.ConfigProvider;
import de.rexlmanu.paperpluginstarter.config.MessageConfig;
import de.rexlmanu.paperpluginstarter.internal.BasePlugin;
import de.rexlmanu.paperpluginstarter.internal.lifecycle.annotations.OnPluginEnable;
import de.rexlmanu.paperpluginstarter.internal.lifecycle.component.Component;
import lombok.RequiredArgsConstructor;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.command.CommandSender;

@Component
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class ReloadCommand {
  private final CommandManager<CommandSender> commandManager;
  private final MiniMessage miniMessage;
  private final ConfigProvider configProvider;
  private final BasePlugin basePlugin;

  @OnPluginEnable
  public void registerCommands() {
    this.commandManager.command(
        this.commandManager
            .commandBuilder("starter", ArgumentDescription.of("Reload the plugin"))
            .permission("starter.command.reload")
            .literal("reload")
            .handler(
                context -> {
                  this.basePlugin.onReload();

                  context
                      .getSender()
                      .sendMessage(
                          this.miniMessage.deserialize(
                              this.configProvider.get(MessageConfig.class).pluginReloaded()));
                }));
  }
}
