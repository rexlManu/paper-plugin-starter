package de.rexlmanu.paperpluginstarter.command;

import cloud.commandframework.CommandManager;
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

  @OnPluginEnable
  public void testCommand() {
    this.commandManager.command(
        this.commandManager
            .commandBuilder("test")
            .handler(
                context ->
                    context
                        .getSender()
                        .sendMessage(this.miniMessage.deserialize("<rainbow>Test</rainbow>"))));
  }
}
