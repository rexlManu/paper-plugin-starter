package de.rexlmanu.paperpluginstarter.command;

import com.google.inject.Inject;
import de.rexlmanu.paperpluginstarter.config.ConfigProvider;
import de.rexlmanu.paperpluginstarter.config.MessageConfig;
import de.rexlmanu.paperpluginstarter.internal.lifecycle.component.Component;
import lombok.RequiredArgsConstructor;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.incendo.cloud.annotations.Command;
import org.incendo.cloud.annotations.Permission;

@Component
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class TestCommand {
  private final ConfigProvider configProvider;
  private final MiniMessage miniMessage;

  @Command("test give <material>")
  @Permission("starter.command.test")
  public void testCommand(Player sender, Material material) {
    sender.getInventory().addItem(new ItemStack(material));
    sender.sendMessage(
        this.miniMessage.deserialize(this.configProvider.get(MessageConfig.class).itemReceived()));
  }
}
