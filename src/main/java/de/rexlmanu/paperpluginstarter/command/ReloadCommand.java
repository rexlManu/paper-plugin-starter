package de.rexlmanu.paperpluginstarter.command;

import static net.kyori.adventure.text.Component.text;

import com.google.inject.Inject;
import de.rexlmanu.boilerplate.BasePlugin;
import de.rexlmanu.boilerplate.lifecycle.annotations.OnPluginEnable;
import de.rexlmanu.boilerplate.lifecycle.component.Component;
import de.rexlmanu.boilerplate.message.MessageProvider;
import de.rexlmanu.paperpluginstarter.config.MessageConfig;
import lombok.RequiredArgsConstructor;
import org.bukkit.command.CommandSender;
import org.incendo.cloud.CommandManager;
import org.incendo.cloud.minecraft.extras.RichDescription;

@Component
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class ReloadCommand {
  private final CommandManager<CommandSender> commandManager;
  private final MessageProvider messageProvider;
  private final BasePlugin basePlugin;

  @OnPluginEnable
  public void registerCommands() {
    this.commandManager.command(
        this.commandManager
            .commandBuilder("starter", RichDescription.of(text("Reload the plugin.")))
            .permission("starter.command.reload")
            .literal("reload")
            .handler(
                context -> {
                  this.basePlugin.onReload();

                  this.messageProvider.send(context.sender(), MessageConfig::pluginReloaded);
                }));
  }
}
