package de.rexlmanu.paperpluginstarter.command;

import cloud.commandframework.ArgumentDescription;
import cloud.commandframework.CommandManager;
import de.rexlmanu.paperpluginstarter.internal.BasePlugin;
import de.rexlmanu.paperpluginstarter.internal.lifecycle.annotations.OnPluginEnable;
import de.rexlmanu.paperpluginstarter.internal.lifecycle.component.Component;
import lombok.RequiredArgsConstructor;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.command.CommandSender;

@Component
@RequiredArgsConstructor(onConstructor = @__(@com.google.inject.Inject))
public class TestCommand {
  private final CommandManager<CommandSender> commandManager;
  private final MiniMessage miniMessage;
  private final BasePlugin plugin;

  @OnPluginEnable
  public void registerCommands() {
    this.commandManager.command(
        this.commandManager
            .commandBuilder("starter", ArgumentDescription.of("Reloads the plugin"))
            .permission("starter.command.reload")
            .literal("reload")
            .handler(
                context -> {
                  this.plugin.onReload();

                  context
                      .getSender()
                      .sendMessage(this.miniMessage.deserialize("<green>Plugin reloaded."));
                }));
  }
}
