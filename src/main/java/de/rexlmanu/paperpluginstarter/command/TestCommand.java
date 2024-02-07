package de.rexlmanu.paperpluginstarter.command;

import com.google.inject.Inject;
import de.rexlmanu.boilerplate.lifecycle.component.Component;
import de.rexlmanu.paperpluginstarter.config.message.MessageConfig;
import de.rexlmanu.paperpluginstarter.config.message.MessageProvider;
import lombok.RequiredArgsConstructor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.incendo.cloud.annotations.Argument;
import org.incendo.cloud.annotations.Command;
import org.incendo.cloud.annotations.Permission;

@Component
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class TestCommand {
  private final MessageProvider messageProvider;

  @Command("test give <material>")
  @Permission("starter.command.test")
  public void testCommand(Player sender, @Argument("material") Material material) {
    sender.getInventory().addItem(new ItemStack(material));

    this.messageProvider.send(sender, MessageConfig::itemReceived);
  }
}
