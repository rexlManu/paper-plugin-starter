package de.rexlmanu.paperpluginstarter;

import com.google.inject.Inject;
import com.google.inject.Injector;
import de.rexlmanu.paperpluginstarter.internal.lifecycle.component.Component;
import de.rexlmanu.paperpluginstarter.internal.lifecycle.annotations.OnPluginDisable;
import de.rexlmanu.paperpluginstarter.internal.lifecycle.annotations.OnPluginEnable;
import de.rexlmanu.paperpluginstarter.internal.lifecycle.annotations.OnPluginReload;
import de.rexlmanu.paperpluginstarter.internal.lifecycle.task.TimedTask;
import io.papermc.paper.threadedregions.scheduler.ScheduledTask;
import lombok.RequiredArgsConstructor;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.slf4j.Logger;

@Component
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class PlayerJoinListener implements Listener {
  public static class Test {
    private final Logger logger;

    @Inject
    public Test(Logger logger) {
      this.logger = logger;
    }

    @TimedTask(period = 1000)
    public void test() {
      this.logger.info("This is a scheduled task, it runs every 1000ms.");
    }
  }

  private final MiniMessage miniMessage;
  private final Logger logger;
  private final Server server;
  private final Injector injector;

  @EventHandler
  public void handleJoin(PlayerJoinEvent event) {
    event.joinMessage(this.miniMessage.deserialize("<gray><player> has entered the server.",
        Placeholder.unparsed("player", event.getPlayer().getName())));

    this.logger.info("{} has joined the server.", event.getPlayer().getName());
  }

  @OnPluginEnable
  public void loadData() {
    this.logger.info("Loading data...");

    Test instance = this.injector.getInstance(Test.class);

    this.logger.info("Test instance: {}", instance);
  }

  @OnPluginDisable
  public void saveData() {
    this.logger.info("Saving data...");
  }

  @OnPluginReload
  public void reloadData() {
    this.logger.info("Reloading data...");
  }

  @TimedTask(period = 500)
  public void tellPlayerHesAwesome(ScheduledTask task) {
    this.logger.info("This is a scheduled task, it runs every 500ms.");

    for (Player onlinePlayer : this.server.getOnlinePlayers()) {
      onlinePlayer.sendMessage(this.miniMessage.deserialize("<gray>You are awesome!"));
    }
  }
}
