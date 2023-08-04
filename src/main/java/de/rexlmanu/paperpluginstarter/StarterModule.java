package de.rexlmanu.paperpluginstarter;

import com.google.inject.AbstractModule;
import com.google.inject.name.Names;
import de.rexlmanu.paperpluginstarter.utility.PluginLogger;
import io.papermc.paper.plugin.configuration.PluginMeta;
import java.nio.file.Path;
import java.util.logging.Logger;
import lombok.RequiredArgsConstructor;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import net.kyori.adventure.text.minimessage.tag.standard.StandardTags;
import org.bukkit.Server;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.ServicesManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

@RequiredArgsConstructor
public class StarterModule extends AbstractModule {
  private final JavaPlugin plugin;

  @Override
  protected void configure() {
    this.bind(JavaPlugin.class).toInstance(this.plugin);
    this.bind(Server.class).toInstance(this.plugin.getServer());
    this.bind(PluginManager.class).toInstance(this.plugin.getServer().getPluginManager());
    this.bind(ServicesManager.class).toInstance(this.plugin.getServer().getServicesManager());
    this.bind(Path.class)
        .annotatedWith(Names.named("dataFolder"))
        .toInstance(this.plugin.getDataFolder().toPath());
    this.bind(Logger.class).annotatedWith(PluginLogger.class).toInstance(this.plugin.getLogger());
    this.bind(PluginMeta.class).toInstance(this.plugin.getPluginMeta());
    this.bind(BukkitScheduler.class).toInstance(this.plugin.getServer().getScheduler());

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
  }
}
