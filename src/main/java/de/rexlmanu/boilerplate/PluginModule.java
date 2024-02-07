package de.rexlmanu.boilerplate;

import com.destroystokyo.paper.entity.ai.MobGoals;
import com.google.inject.AbstractModule;
import com.google.inject.name.Names;
import de.rexlmanu.boilerplate.command.CommandModule;
import de.rexlmanu.boilerplate.event.EventModule;
import de.rexlmanu.boilerplate.lifecycle.annotations.DataDirectory;
import de.rexlmanu.boilerplate.lifecycle.annotations.PluginLogger;
import de.rexlmanu.boilerplate.lifecycle.task.TaskModule;
import io.papermc.paper.datapack.DatapackManager;
import io.papermc.paper.plugin.configuration.PluginMeta;
import io.papermc.paper.threadedregions.scheduler.AsyncScheduler;
import io.papermc.paper.threadedregions.scheduler.GlobalRegionScheduler;
import io.papermc.paper.threadedregions.scheduler.RegionScheduler;
import java.nio.file.Path;
import java.util.logging.Logger;
import lombok.RequiredArgsConstructor;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import net.kyori.adventure.text.minimessage.tag.standard.StandardTags;
import org.bukkit.Server;
import org.bukkit.command.CommandMap;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.help.HelpMap;
import org.bukkit.inventory.ItemFactory;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.ServicesManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.messaging.Messenger;
import org.bukkit.potion.PotionBrewer;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.structure.StructureManager;

@RequiredArgsConstructor
public class PluginModule extends AbstractModule {
  private final BasePlugin plugin;

  @Override
  protected void configure() {
    Server server = this.plugin.getServer();
    this.install(new EventModule(this.plugin, server.getPluginManager()));
    this.install(new TaskModule(server.getAsyncScheduler(), this.plugin));
    this.install(new CommandModule());

    this.bind(BasePlugin.class).toInstance(this.plugin);
    this.bind(JavaPlugin.class).toInstance(this.plugin);
    //noinspection UnstableApiUsage
    this.bind(PluginMeta.class).toInstance(this.plugin.getPluginMeta());
    this.bind(Path.class)
        .annotatedWith(DataDirectory.class)
        .toInstance(this.plugin.getDataFolder().toPath());
    this.bind(Server.class).toInstance(server);
    this.bind(FileConfiguration.class).toInstance(this.plugin.getConfig());

    // Logging
    this.bind(ComponentLogger.class).toInstance(this.plugin.getComponentLogger());
    this.bind(Logger.class).annotatedWith(PluginLogger.class).toInstance(this.plugin.getLogger());
    this.bind(org.slf4j.Logger.class).toInstance(this.plugin.getSLF4JLogger());

    // MiniMessage
    this.bind(MiniMessage.class).toInstance(MiniMessage.miniMessage());
    this.bind(MiniMessage.class)
        .annotatedWith(Names.named("colorMiniMessage"))
        .toInstance(
            MiniMessage.builder()
                .tags(
                    TagResolver.builder()
                        .resolvers(StandardTags.color(), StandardTags.decorations())
                        .build())
                .build());

    // Instances from the server
    this.bind(PluginManager.class).toInstance(server.getPluginManager());
    this.bind(ServicesManager.class).toInstance(server.getServicesManager());
    this.bind(AsyncScheduler.class).toInstance(server.getAsyncScheduler());
    this.bind(BukkitScheduler.class).toInstance(server.getScheduler());
    this.bind(RegionScheduler.class).toInstance(server.getRegionScheduler());
    this.bind(GlobalRegionScheduler.class).toInstance(server.getGlobalRegionScheduler());
    this.bind(CommandMap.class).toInstance(server.getCommandMap());
    this.bind(ConsoleCommandSender.class).toInstance(server.getConsoleSender());
    this.bind(DatapackManager.class).toInstance(server.getDatapackManager());
    this.bind(HelpMap.class).toInstance(server.getHelpMap());
    this.bind(ItemFactory.class).toInstance(server.getItemFactory());
    this.bind(Messenger.class).toInstance(server.getMessenger());
    this.bind(MobGoals.class).toInstance(server.getMobGoals());
    this.bind(PotionBrewer.class).toInstance(server.getPotionBrewer());
    this.bind(ScoreboardManager.class).toInstance(server.getScoreboardManager());
    this.bind(StructureManager.class).toInstance(server.getStructureManager());
  }
}
